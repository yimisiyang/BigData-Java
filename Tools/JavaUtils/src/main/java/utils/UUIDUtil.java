package utils;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

/**
 *  UUID生成，32位，24位（Base64编码），16位（32位前半后半异或）
 * @author angilin
 *
 */
public class UUIDUtil {

	public static void main(String[] args) {

		// UUID
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);
		System.out.println(uuid.toString().replaceAll("-", ""));

		String previous = uuid.toString().replaceAll("-", "").substring(0, 16);
		String next = uuid.toString().replaceAll("-", "").substring(16, 32);

		System.out.println(previous);
		System.out.println(next);

		byte[] a = hexStringToBytes(previous);
		byte[] b = hexStringToBytes(next);

		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			Integer ddfd = a[i] ^ b[i];
			byte c1 = ddfd.byteValue();
			System.out.print(Integer.toHexString(0xFF & a[i]) + "^");
			System.out.print(Integer.toHexString(0xFF & b[i]) + "=");
			System.out.print(Integer.toHexString(0xFF & c1));
			System.out.println();

			String dd = Integer.toHexString(0xFF & c1);
			s.append(dd.length() < 2 ? "0" + dd : dd);
		}
		System.out.println(s.toString());

		/*
		 * //如果byte[]没有转String，输出类似[B@4b28899a 
		 * System.out.println(new Base64().encode(uuid.toString().replaceAll("-", "").getBytes()));
		 * //如果没有把uuid作16进制转String操作，转出的字符串会更长（等于把每个char转byte了）
		 * System.out.println(new Base64().encodeToString(uuid.toString().replaceAll("-","").getBytes()));
		 */

		//BASE64编码， 需要先作16进制转String操作
		System.out.println(new Base64().encodeToString(hexStringToBytes(uuid
				.toString().replaceAll("-", ""))));
		

		System.out.println(createUUID());
		System.out.println(createUUID16());
		System.out.println(createUUID24());
		
		
		
		/*Set<String> set = new HashSet<String>();
		for(int i=0;i<1000000;i++){
			set.add(createUUID16());
		}
		System.out.println(set.size());*/
	}

	/**
	 * 生成32位uuid
	 * @return
	 */
	public static String createUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 生成16位uuid，由32位uuid的前16位和后16位做异或操作得到
	 * @return
	 */
	public static String createUUID16() {
		String uuid = createUUID();
		//16进制字符串转byte[]
		byte[] previous = hexStringToBytes(uuid.substring(0, 16));
		byte[] next = hexStringToBytes(uuid.substring(16, 32));
		
		StringBuilder uuid16 = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			Integer byteValue = previous[i] ^ next[i];
			String byteValueHex = Integer.toHexString(0xFF & byteValue);
			uuid16.append(byteValueHex.length() < 2 ? ("0" + byteValueHex)
					: byteValueHex);
		}
		return uuid16.toString();
	}
	
	/**
	 * 生成24位uuid，由32位uuid做base64编码得到
	 * @return
	 */
	public static String createUUID24(){
		String uuid = createUUID();
		return new Base64().encodeToString(hexStringToBytes(uuid));
	}

	/**
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
	 * @param src byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * Convert hex string to byte[]
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * Convert char to byte
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
