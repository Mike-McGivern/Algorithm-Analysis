import java.math.BigInteger;

public class Karatsuba {
   public static BigInteger multiply(BigInteger x, BigInteger y) {
      int N = Math.max(x.bitLength(), y.bitLength());
      
      if(N <= 2000) 
         return x.multiply(y);
      
      N = (N / 2) + (N % 2);
      
      BigInteger b = x.shiftRight(N);
      BigInteger a = x.subtract(b.shiftLeft(N));
      BigInteger d = y.shiftRight(N);
      BigInteger c = y.subtract(d.shiftLeft(N));
      
      BigInteger ac = multiply(a, c);
      BigInteger bd = multiply(b, d);
      BigInteger abcd = multiply(a.add(b), c.add(d));
      
      BigInteger mid = abcd.subtract(ac).subtract(bd);
      return ac.add(mid.shiftLeft(N)).add(bd.shiftLeft(2 * N));
   }
}