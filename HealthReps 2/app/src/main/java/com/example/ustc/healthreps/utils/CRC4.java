package com.example.ustc.healthreps.utils;

//CRC4编码
public class CRC4 {
	private byte sbox[] = new byte[256]; // 待编码的字符数组
	private byte key[] = new byte[256]; // 数字键值
	private byte k; // 键值指针
	private int m, n, i, j, ilen;

	public CRC4() {
		for (int x = 0; x < 256; x++) {
			sbox[x] = 0;
		}
		for (int x = 0; x < 256; x++) {
			sbox[x] = 0;
		}
	}

	// Encrypt
	public byte[] Encrypt(byte[] pszText, byte[] pszKey) {
		i = 0;
		j = 0;
		n = 0;
		ilen = pszKey.length;

		// 初始化键值
		for (m = 0; m < 256; m++) {
			key[m] = pszKey[m % ilen];
			sbox[m] = (byte) m;
		}

		for (m = 0; m < 256; m++) {
			n = (n + sbox[m] + key[m]) & 0xff;
			// Utils.SWAP(sbox[m], sbox[n]);
			byte c = sbox[m];
			sbox[m] = sbox[n];
			sbox[n] = c;
		}

		ilen = pszText.length;
		for (m = 0; m < ilen; m++) {
			i = (i + 1) & 0xff;
			j = (j + sbox[i]) & 0xff;
			// Utils.SWAP(sbox[i], sbox[j]);
			byte c = sbox[i];
			sbox[i] = sbox[j];
			sbox[j] = c;
			k = sbox[(sbox[i] + sbox[j]) & 0xff];
			if (k == pszText[m]) {
				k = 0;
			}
			pszText[m] ^= k;
		}
		return pszText;
	}

	// Decrypt
	public byte[] Decrypt(byte[] pszText, byte[] pszKey) {
		return Encrypt(pszText, pszKey);
	}
}
