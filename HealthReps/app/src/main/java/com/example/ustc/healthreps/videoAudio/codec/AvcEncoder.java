package com.example.ustc.healthreps.videoAudio.codec;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;

public class AvcEncoder
{
    private int mFrameIndex = 0;
    private static final int FRAME_RATE = 20;

    private MediaCodec mediaCodec;
    int m_width;
    int m_height;
    byte[] m_info = null;

    private static final String MIME_TYPE = "video/avc";
    boolean colorF=true;

    private byte[] yuv420 = null;
    private byte[] yuv420sp = null;
    //	private byte[] yuv420mirr = null;
    @SuppressLint("NewApi")
    public AvcEncoder(int width, int height, int framerate, int bitrate) {

        m_width  = width;
        m_height = height;
        yuv420 = new byte[width*height*3/2];
        yuv420sp = new byte[width*height*3/2];
//		yuv420mirr = new byte[width*height*3/2];

        try {
            mediaCodec = MediaCodec.createEncoderByType("video/avc");
        }catch (IOException e){
            e.printStackTrace();
        }

        MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
        int colorFormat = selectColorFormat(codecInfo, MIME_TYPE);
        if(colorFormat==19){
            colorF=false;
        }else{
            colorF=true;
        }
        //TODO
        MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);///如果添加旋转适配 更换 width 和 height 的位置
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,colorFormat); //可选择使用与不同硬件的方法关键
        //MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar.COLOR_FormatSurface
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);

        try {
            mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        }catch (IOException e){
            e.printStackTrace();
        }

        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mediaCodec.start();
    }

    @SuppressLint("NewApi")
    public void close() {
        try {
            mediaCodec.stop();
            mediaCodec.release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public int offerEncoder(byte[] input, byte[] output)
    {
        int pos = 0;
        //TODO
        if(colorF){
            YV12toYUV420PackedSemiPlanar(input, yuv420, m_width, m_height);//yuv420sp
//	2016		rotateYUV240SPSemifront(yuv420sp,yuv420,m_width,m_height);//yuv420mirr
//			rotateYUV240SPSemi(yuv420sp,yuv420,m_width,m_height);//yuv420mirr
        }else{
            swapYV12toI420(input, yuv420, m_width, m_height);//yuv420sp
//	2016		rotateYUV240SPfront(yuv420sp,yuv420,m_width,m_height);//yuv420mirr
//			rotateYUV240SP(yuv420sp,yuv420,m_width,m_height);//yuv420mirr
        }
//		mirrorRotate(yuv420mirr,yuv420,m_width,m_height);
        try {
            ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
            ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
            int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);//10000
            if (inputBufferIndex >= 0)
            {
                long timestamp = System.nanoTime() / 1000;//= mFrameIndex++ * 1000000 / FRAME_RATE;
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(yuv420);//input
                mediaCodec.queueInputBuffer(inputBufferIndex, 0, yuv420.length, timestamp, 0);//input
            }
            else{
                Log.v("inputBufferIndex:", String.valueOf(inputBufferIndex));
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo,0);
            Log.v("outputBufferIndex:", String.valueOf(outputBufferIndex));
            int trytimes = 0;
            while(outputBufferIndex<0){
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo,0);
                //Thread.sleep(10);
//	lzlzzllzlzlz        	Log.v("Yuv420 Length:" + String.valueOf(yuv420.length), "TryTimes:  "+ String.valueOf(++trytimes));
            }
            while (outputBufferIndex >= 0)
            {
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                if(m_info != null)
                {
                    System.arraycopy(outData, 0,  output, pos, outData.length);
                    pos += outData.length;
                    if(outData.length==0){
                        Log.v("outData:", "outData=0");
                    }
                }
                else
                {
                    Log.v("m_info:", "m_info=null");
                    ByteBuffer spsPpsBuffer = ByteBuffer.wrap(outData);
                    if (spsPpsBuffer.getInt() == 0x00000001)
                    {
                        m_info = new byte[outData.length];
                        System.arraycopy(outData, 0, m_info, 0, outData.length);
                    }
                    else
                    {
                        return -1;
                    }
                }
                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
            }

            if(output[4] == 0x65) //key frame
            {
                System.arraycopy(output, 0,  yuv420, 0, pos);//input
                System.arraycopy(m_info, 0,  output, 0, m_info.length);
                System.arraycopy(yuv420, 0,  output, m_info.length, pos);//input
                pos += m_info.length;
                if(m_info.length==0){
                    Log.v("m_info.length:", "m_info.length=0");
                }
            }else{
                Log.v("output[4]", String.valueOf(output[4]));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return pos;
    }

    private void swapYV12toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height)
    {
        System.arraycopy(yv12bytes, 0, i420bytes, 0,width*height);
        System.arraycopy(yv12bytes, width*height+width*height/4, i420bytes, width*height,width*height/4);
        System.arraycopy(yv12bytes, width*height, i420bytes, width*height+width*height/4,width*height/4);
    }
    private void rotateYUV240SPfront(byte[] src,byte[] des,int width,int height)
    {
        int wh = width * height;
        //旋转Y  
        int k = 0;
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                des[k] = src[wh-i-j*width-1];
                k++;
            }
        }

        for(int i=0;i<width/2;i++) {
            for(int j=0;j<height/2;j++) {
                des[k] = src[wh+wh/4-i-j*width/2-1];
                des[k+width*height/4]=src[wh*5/4+wh/4-i-j*width/2-1];
                k++;
            }
        }
    }
    private void rotateYUV240SP(byte[] src,byte[] des,int width,int height)
    {
        int wh = width * height;
        //旋转Y  
        int k = 0;
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                des[k] = src[width*j + i];
                k++;
            }
        }

        for(int i=0;i<width/2;i++) {
            for(int j=0;j<height/2;j++) {
                des[k] = src[wh+ width/2*j + i];
                des[k+width*height/4]=src[wh*5/4 + width/2*j + i];
                k++;
            }
        }
    }
    private void mirrorRotate(byte[] src,byte[] des,int width,int height)
    {
        int wh=width*height;

        int k=0;
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                des[k] = src[width*(i+1)-j-1];
                k++;
            }
        }

        for(int i=0;i<height/2;i++) {
            for(int j=0;j<width/2;j++) {
                des[k] = src[wh+ width/2*(i+1)-j-1];
                des[k+width*height/4]=src[wh*5/4 + width/2*(i+1)-j-1];
                k++;
            }
        }
    }
    private void YV12toYUV420PackedSemiPlanar(byte[] input, byte[] output, int width, int height) {
        /* 
         * COLOR_TI_FormatYUV420PackedSemiPlanar is NV12
         * We convert by putting the corresponding U and V bytes together (interleaved).
         */
        int frameSize = width * height;//final 
        int qFrameSize = width * height/4;//final 

        System.arraycopy(input, 0, output, 0, frameSize);
        for (int i = 0; i < (qFrameSize); i++) {
            output[frameSize + i*2] = (input[frameSize + qFrameSize + i]);
            output[frameSize + i*2 + 1] = (input[frameSize + i]);
        }
    }
    private void rotateYUV240SPSemifront(byte[] src,byte[] des,int width,int height)
    {
        int wh = width * height;
        //旋转Y  
        int k = 0;
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                des[k] = src[wh-i-width*j-1];
                k++;
            }
        }

        for(int i=0;i<width/2;i++) {
            for(int j=0;j<height/2;j++) {
                des[k]=src[width*height+wh/2-i*2-width*j-2];
                des[++k]=src[width*height+wh/2-i*2-width*j-2+1];
                k++;
            }
        }
    }
    private void rotateYUV240SPSemi(byte[] src,byte[] des,int width,int height)
    {
        int wh = width * height;
        //旋转Y  
        int k = 0;
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                des[k] = src[width*j + i];
                k++;
            }
        }

        for(int i=0;i<width/2;i++) {
            for(int j=0;j<height/2;j++) {
                des[k]=src[width*height+width*j+i*2];
                des[++k]=src[width*height+width*j+i*2+1];
                k++;
            }
        }
    }
    /**
     * Returns the first codec capable of encoding the specified MIME type, or null if no
     * match was found.
     */
    private static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }
    /**
     * Returns a color format that is supported by the codec and by this test code.  If no
     * match is found, this throws a test failure -- the set of formats known to the test
     * should be expanded for new platforms.
     */
    private static int selectColorFormat(MediaCodecInfo codecInfo, String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            int colorFormat = capabilities.colorFormats[i];
            if (isRecognizedFormat(colorFormat)) {
                return colorFormat;
            }
        }
//        fail("couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        Log.v("problem","couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        return 0;   // not reached
    }
    /**
     * Returns true if this is a color format that this test code understands (i.e. we know how
     * to read and generate frames in this format).
     */
    private static boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {
            // these are the formats we know how to handle for this test
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                return false;
        }
    }
}


