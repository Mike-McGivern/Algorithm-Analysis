import java.math.BigInteger;
import java.security.SecureRandom; //used for random BigInteger generation
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.ClassNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedInputStream;

public class Assignment3 {

   private static int totalBlocks = 0; 
   private static int lastBlockLength = 32;// default block size
   
   public static void main(String[] args) {
      makeKeys();
      System.out.println("Keys Generated");
   
      encrypt();
      decrypt(); 
   }
   
   public static void makeKeys() {
      BigInteger x = BigInteger.probablePrime(2048, new SecureRandom()); 
      BigInteger y = BigInteger.probablePrime(2048, new SecureRandom());
      while (x.equals(y)) {
         y = BigInteger.probablePrime(2048, new SecureRandom());
      }
   
      BigInteger N = x.multiply(y);
      BigInteger phi = x.subtract(BigInteger.ONE).multiply(y.subtract(BigInteger.ONE));
      
      SecureRandom rand = new SecureRandom();
      BigInteger p;
      do {
         p = BigInteger.probablePrime(512, rand); // smaller prime for exponent
      } while (!phi.gcd(p).equals(BigInteger.ONE));
   
   
   
      BigInteger s = p.modInverse(phi); //p.gcd(phi)
      

      try(ObjectOutputStream pubOut = new ObjectOutputStream(new FileOutputStream("pubKey.dat"))) {
         pubOut.writeObject(p);
         pubOut.writeObject(N);
         
      } catch(IOException e) {
         System.out.println("Error writing public key");
         System.exit(1);
      }
      
      try(ObjectOutputStream privOut = new ObjectOutputStream(new FileOutputStream("privKey.dat"))) {
         privOut.writeObject(s);
         privOut.writeObject(N);
         
      } catch(IOException e) {
         System.out.println("Error writing private key");
         System.exit(1);
      }
      System.out.println("p: \n" + p + "\ns: \n" + s + "\nN: \n" + N + "\nphi: \n" + phi);
   }
   
   public static void encrypt() {
      try {
         ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream("pubKey.dat"));
         BigInteger p = (BigInteger) keyIn.readObject();
         BigInteger N = (BigInteger) keyIn.readObject();
         
         keyIn.close();
         
         BufferedInputStream reader = new BufferedInputStream(new FileInputStream("Test.txt"));
         ObjectOutputStream pubOut = new ObjectOutputStream(new FileOutputStream("EncryptedTest.dat"));
         
      
         int blockSize = 32;
         byte[] block = new byte[blockSize];
         int index = 0;
         int bytesRead;
      
         while ((bytesRead = reader.read(block)) != -1) {
            if (bytesRead < blockSize) {
               lastBlockLength = bytesRead; //tracks length of last block before padding
               for (int i = bytesRead; i < blockSize; i++) { //handle final block with unfilled space at the end
                  block[i] = 32; // pad with spaces
               }
            
            }
         
            BigInteger M = encodeBlock(block); //encode the block of characters as a BigInteger
            BigInteger C = M.modPow(p, N); //apply public key encryption (what the sender would do)
            pubOut.writeObject(C); //write encrpyted block to the object out
            totalBlocks++;
         }
      
         
         reader.close();
         pubOut.close();
      } catch(FileNotFoundException e) {
         System.out.println("File Not Found Exception thrown for encrypt method. Either Test.txt or pubKey.dat is missing");
         System.exit(1);
      } catch(IOException e) {
         System.out.println("IO exception thrown for encrypt method");
         System.exit(1);
      } catch(ClassNotFoundException e) {
         System.out.println("ClassNotFound Exception thrown for encypt method");
         System.exit(1);
      } catch(Exception e) {
         System.out.println("General Exception thrown for encypt method");
      }
      System.out.println("Encrpyted file is created");
   }
   
   private static BigInteger encodeBlock(byte[] block) { //modular way to encode each block
      BigInteger M = BigInteger.ZERO;
      for(int i = 0; i < block.length; i++) { // Horners method
         M = M.multiply(BigInteger.valueOf(128)).add(BigInteger.valueOf(block[i]));
      }
      return M;
   }
   
   
   private static void decrypt() {
      try {
         ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream("privKey.dat"));
         ObjectInputStream cipherIn = new ObjectInputStream(new FileInputStream("EncryptedTest.dat"));
         BufferedWriter messageOut = new BufferedWriter(new FileWriter("Decrypted.txt")); 
      
         BigInteger s = (BigInteger) keyIn.readObject();
         BigInteger N = (BigInteger) keyIn.readObject();
      
         int blockCount = 0;
         while (true) {
            try {
               BigInteger C = (BigInteger) cipherIn.readObject(); //read in chipher text
               BigInteger M = C.modPow(s, N); //apply private key decryption (What the reciver would do)
            
                // Decode using reverse Horner's method
               char[] chars = new char[32];
               for (int i = 31; i >= 0; i--) {
                  chars[i] = (char) M.mod(BigInteger.valueOf(128)).intValue(); 
                  M = M.divide(BigInteger.valueOf(128));
               }
               blockCount++;
               if(blockCount == totalBlocks) {
               //reverts last block which was padded to original state
                  messageOut.write(chars, 0, lastBlockLength);
               } else messageOut.write(chars);
            } catch (EOFException eof) {
               break; // end of encrypted file
            }
         }
         messageOut.flush();
         messageOut.close();      
      } catch (FileNotFoundException e) {
         System.out.println("Private key or encrypted file not found.");
         System.exit(1);
      } catch (IOException e) {
         System.out.println("IO error during decryption.");
         System.exit(1);
      } catch (ClassNotFoundException e) {
         System.out.println("Class not found while reading encrypted data.");
         System.exit(1);
      }
      System.out.println("Decrpyted file is created, compare it to the original file");
   
   
   }
}