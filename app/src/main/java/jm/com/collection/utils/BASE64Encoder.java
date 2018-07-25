package jm.com.collection.utils;

public class BASE64Encoder
{
  private static char[] codec_table = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 
    'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 
    'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
  
  public String encode(byte[] a)
  {
    int totalBits = a.length * 8;
    int nn = totalBits % 6;
    int curPos = 0;
    StringBuffer toReturn = new StringBuffer();
    while (curPos < totalBits)
    {
      int bytePos = curPos / 8;
      switch (curPos % 8)
      {
      case 0: 
        toReturn.append(codec_table[((a[bytePos] & 0xFC) >> 2)]);
        break;
      case 2: 
        toReturn.append(codec_table[(a[bytePos] & 0x3F)]);
        break;
      case 4: 
        if (bytePos == a.length - 1)
        {
          toReturn.append(codec_table[((a[bytePos] & 0xF) << 2 & 0x3F)]);
        }
        else
        {
          int pos = ((a[bytePos] & 0xF) << 2 | (a[(bytePos + 1)] & 0xC0) >> 6) & 0x3F;
          toReturn.append(codec_table[pos]);
        }
        break;
      case 6: 
        if (bytePos == a.length - 1)
        {
          toReturn.append(codec_table[((a[bytePos] & 0x3) << 4 & 0x3F)]);
        }
        else
        {
          int pos = ((a[bytePos] & 0x3) << 4 | (a[(bytePos + 1)] & 0xF0) >> 4) & 0x3F;
          toReturn.append(codec_table[pos]);
        }
        break;
      }
      curPos += 6;
    }
    if (nn == 2) {
      toReturn.append("==");
    } else if (nn == 4) {
      toReturn.append("=");
    }
    return toReturn.toString();
  }
}
