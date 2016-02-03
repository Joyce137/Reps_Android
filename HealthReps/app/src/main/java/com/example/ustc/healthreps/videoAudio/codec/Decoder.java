package com.example.ustc.healthreps.videoAudio.codec;

import com.example.ustc.healthreps.videoAudio.androiddecoder_log.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

@SuppressLint("NewApi")
public class Decoder {
	private static final String TAG = "StudyCamera";
	private MediaCodec mMediaDecoder;
	private int mFrameIndex = 0;

	public Decoder(Surface surface, String mime, int width, int height, int bitrate, int framerate){
		Log.d(TAG,"setupDecoder surface:"+surface+" mime:"+mime+" w:"+width+" h:"+height);
		try {
			mMediaDecoder = MediaCodec.createDecoderByType(mime);
		}catch (IOException e){
			e.printStackTrace();
		}

		MediaCodecInfo codecInfo = selectCodec(mime);
		int colorFormat = selectColorFormat(codecInfo, mime);

		MediaFormat mediaFormat = MediaFormat.createVideoFormat(mime,width,height);
		mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
		mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
		mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, colorFormat);
		//MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar.COLOR_FormatYUV420Planar
		mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);

		mMediaDecoder.configure(mediaFormat, surface, null, 0);
		mMediaDecoder.start();
	}
	public void close() {
		try {
			mMediaDecoder.stop();
			mMediaDecoder.release();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	public void offerDecoder(byte[] input,int length, int framerate) {
		try {
			ByteBuffer[] inputBuffers = mMediaDecoder.getInputBuffers();
			int inputBufferIndex = mMediaDecoder.dequeueInputBuffer(-1);
			if (inputBufferIndex >= 0) {
				ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
				long timestamp = System.nanoTime() / 1000;// = mFrameIndex++ * 1000000 / framerate
				Log.d(TAG,"offerDecoder timestamp: " +timestamp+" inputSize: "+length + " bytes");
				inputBuffer.clear();
				inputBuffer.put(input,0,length);
				mMediaDecoder.queueInputBuffer(inputBufferIndex, 0, length, timestamp, 0);
			}

			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			int outputBufferIndex = mMediaDecoder.dequeueOutputBuffer(bufferInfo,0);
			while (outputBufferIndex >= 0) {
				Log.d(TAG,"offerDecoder OutputBufSize:"+bufferInfo.size+ " bytes written");

				//If a valid surface was specified when configuring the codec,
				//passing true renders this output buffer to the surface.
				mMediaDecoder.releaseOutputBuffer(outputBufferIndex, true);
				outputBufferIndex = mMediaDecoder.dequeueOutputBuffer(bufferInfo, 0);
			}
		} catch (Throwable t) {
			t.printStackTrace();
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
