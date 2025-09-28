public class FFTMultipler {
   public static int[] multiply(int[] a, int[] b) {
      int n = 1;
      while(n < a.length + b.length) n <<= 1;
      
      Complex[] A = new Complex[n];
      Complex[] B = new Complex[n];
      
      for(int i = 0; i < n; i++) {
         A[i] = i < a.length ? new Complex(a[i], 0) : Complex.ZERO;
         B[i] = i < b.length ? new Complex(b[i], 0) : Complex.ZERO;
      }
   
      fft(A, false);
      fft(B, false);
      for(int i = 0; i < n; i++) A[i].multiply(B[i]);
      fft(A, true);
   
      int[] result = new int[n];
      for(int i = 0; i < n; i++) result[i] = (int) Math.round(A[i].re);
      return result;
   }

   private static void fft(Complex[] a, boolean invert) {
      int n = a.length;
      for(int i = 1, j = 0; i < n; i++) {
         int bit = n >> 1;
         for(; j >= bit; bit >>= 1) j-= bit;
         j += bit;
         if(i < j) {
            Complex tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
         }
      }
   
      for(int len = 2; len <= n; len <<= 1) {
         double angle = 2 * Math.PI / len * (invert ? -1 : 1);
         Complex wlen = new Complex(Math.cos(angle), Math.sin(angle));
         for(int i = 0; i < n; i += len) {
            Complex w = Complex.ONE;
            for(int j = 0; j < len / 2; j++) {
               Complex u = a[i + j];
               Complex v = a[i + j + len / 2].multiply(w);
               w = w.multiply(wlen);
            }
         }
      }
   
      if(invert) {
         for(int i = 0; i < n; i++) a[i] = a[i].divide(n);
      }
   }
   
   private static class Complex {
      double re, im;
      static final Complex ZERO = new Complex(0, 0);
      static final Complex ONE = new Complex(1, 0);
   
      Complex(double re, double im) {this.re = re; this.im = im; }
   
      Complex add(Complex o) {
         return new Complex(re + o.re, im + o.im); }
      Complex subtract(Complex o) {
         return new Complex(re - o.re, im - o.im); }
      Complex multiply(Complex o) {
         return new Complex(re * o.re - im * o.im, re * o.im + im * o.re);
      }
      Complex divide(double val) {
         return new Complex(re / val, im / val); }
   }

}

