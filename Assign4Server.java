import java.util.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat; 

public class Assign4Server {

   public static final int PORT = 5678;
   
   private int MaxUsers;
   private Socket [] users;
   private UserThread [] threads;
   private int numUsers;
   private int itemNum;

   private Vector<SalesFigure> Sales;

   public Assign4Server(int MaxU)  {

      MaxUsers = MaxU;
      users = new Socket[MaxUsers];
      threads = new UserThread[MaxUsers];
      numUsers = 0;
      itemNum = 1;

      Sales = new Vector<SalesFigure>();

      try  {
		 runServer();
      }
      catch (Exception e)  {
	 	 System.out.println("Problem with server");
      }
   }

   public static void main(String [] args)  {
   
      Assign4Server Server = new Assign4Server(5);
   }

   public synchronized void removeClient(int id, String name)  {

      try  {
		 users[id].close();
         System.out.println("Closing client");
      }
      catch (IOException e)  {
	 	System.out.println("Already closed");
      }

      users[id] = null;
      threads[id] = null;
      for (int i = id; i < numUsers - 1; i++)  {

	  	 users[i] = users[i + 1];
      	 threads[i] = threads[i + 1];
         threads[i].setId(i);
      }
      numUsers--;
   }



   private void runServer() throws IOException  {

      ServerSocket s = new ServerSocket(PORT);
      System.out.println("Started: " + s);

      try  {
	 	while(true) {

	     if (numUsers < MaxUsers)  {

	    	try  {
	       
  	        	Socket newSocket = s.accept();
	        	synchronized (this)  {
		
		  		users[numUsers] = newSocket;
	            ObjectInputStream tempInStream = new ObjectInputStream(
		  							newSocket.getInputStream());

		  		String newName = (String) tempInStream.readObject();
		  		threads[numUsers] = new UserThread(newSocket,
						numUsers, newName, tempInStream);
		  		System.out.println("connection " + numUsers + users[numUsers]);
	          	threads[numUsers].start();
                 
		  		numUsers++;
	        	}
	     	}
	     	catch (Exception e)  {
				System.out.println("Problem with connection");
	     	}
	  	   } // end if

		}// end while
      } //end outer try
      finally  {
	 	System.out.println("Server shutting down");
      }
   }

   private class UserThread extends Thread  {

      private Socket mySocket;
      private ObjectInputStream myReader;
      private ObjectOutputStream myWriter;
      private int myID;
      private String myName;

      private UserThread(Socket newSocket, int id, String newName,
                    ObjectInputStream newReader) throws IOException  {

    	 mySocket = newSocket;
         myID = id;
         myName = newName;
	     myReader = newReader;
         myWriter = new ObjectOutputStream(mySocket.getOutputStream());
         myWriter.flush();
         
      }

      public ObjectInputStream getReader()  {
         return myReader;
      }

      public ObjectOutputStream getWriter()  {
         return myWriter;
      }
	
	  public String getMyName() {
	  	return this.myName;
	  }

      public synchronized void setId(int newID)  {
	 		myID = newID;
      }

      public void run()  {
         boolean ok = true;
         while(ok)  {

            String currMsg = null;
            String salesRep = this.getMyName();
            String salesLoc = "";
            double salesAmount = 0.0;
            int itemNumber = 0;
            int x = 0;
	        SalesFigure s = new SalesFigure();
            boolean flag = false;
            String checkSalesRep = null;

            try {
                ObjectOutputStream newWriter = this.getWriter();
                newWriter.writeObject("Choose an option below.\n");
                currMsg = (String) myReader.readObject();
                //System.out.println(currMsg);
                synchronized(this)  {

                   if (currMsg.compareTo("Add Sales") == 0) {

                        newWriter.writeObject(
							"Enter your sales location in box below and press ENTER");
		       			salesLoc = (String) myReader.readObject();
			   
		       			newWriter.writeObject(
			   				"Enter your sales amount in box below and press ENTER.");
			    		try {
			           		salesAmount = Double.parseDouble((String) myReader.readObject());
                       		s = new SalesFigure(itemNum, salesRep, salesLoc, salesAmount);
                       		Sales.addElement(s);
                       		itemNum++;
		               		newWriter.writeObject("Your sales data has been added.");
			     		}
			     		catch (NumberFormatException n) {
			     			newWriter.writeObject("Price entered incorrectly.  Please start over.");
			     		}                    
	           		}
                    else if (currMsg.compareTo("Edit Sales") == 0)  {
                      int i;
                      flag = false;
                      newWriter.writeObject("Enter number of item to be edited " +
												"in box below and press ENTER.");
                      x = Integer.parseInt((String)myReader.readObject());                 
                      for ( i = 0; i < Sales.size() ; i++)  {
                         itemNumber = Sales.elementAt(i).getSalesID();
                         checkSalesRep = Sales.elementAt(i).getSalesRep();
                         if ((itemNumber == x)&&(checkSalesRep.equals(salesRep)))  {
                            flag = true;
                            break;
                         }
                      }
                      
                      if (flag)  {
                      
	                      newWriter.writeObject("Enter correct sales location " +
													"in box below and press ENTER.");
                      	  salesLoc = (String) myReader.readObject();
			   
		     	  		  newWriter.writeObject(
			   	 			   "Enter correct sales amount in box below and press ENTER.");
			    	  	  try {
			           		salesAmount = Double.parseDouble((String) myReader.readObject());
			           		Sales.elementAt(i).setStoreLocation(salesLoc);
                       		Sales.elementAt(i).setAmount(salesAmount);
		               		newWriter.writeObject("Your sales data has been changed.");
			     		  }
			     		  catch (NumberFormatException n) {
			     			newWriter.writeObject("Price entered incorrectly.  Please start over.");
			     		  }
                       }
			     	   else  {
			     	       newWriter.writeObject("Incorrect number chosen.  Please start over.");
                       }
                   }

                   else if (currMsg.compareTo("Delete Sales") == 0)  {
                      int i;
                      flag = false;
                      newWriter.writeObject("Enter number of item to be deleted " +
												"in box below and press ENTER.");
                      x = Integer.parseInt((String)myReader.readObject());                 
                      for ( i = 0; i < Sales.size() ; i++)  {
                         itemNumber = Sales.elementAt(i).getSalesID();
                         checkSalesRep = Sales.elementAt(i).getSalesRep();
                         if ((itemNumber == x)&&(checkSalesRep.equals(salesRep)))  {
							flag = true;
                            Sales.removeElementAt(i);
                            newWriter.writeObject("Sale deleted.");
                      	 
                            break;
                         }
                      }
                      if (!flag) {
			     	       newWriter.writeObject("Incorrect number chosen.  Please start over.");
                      	
                      }
                   }

						



                   else if (currMsg.compareTo("View my sales") == 0)  {
System.out.println("Heremy");
                      newWriter.writeObject("Your sales");
 
                      newWriter.writeObject(String.format("%s    %-15s%-15s%9s","ID", "Name", "Location", "Amount"));
                      
                      for (SalesFigure f : Sales) {
                      	 if (f.getSalesRep().equals(salesRep)){
                      	 
	                       	 newWriter.writeObject(f.toString());
                      	 }
                      }  
                   }

                   else if (currMsg.compareTo("View all sales")==0)  {
						System.out.println("Hereall");
		//System.exit(0);
                      newWriter.writeObject("All sales");
                      newWriter.writeObject(String.format("%s    %-15s%-15s%9s","ID", "Name", "Location", "Amount"));
                      for (SalesFigure f : Sales) {
	                       	 newWriter.writeObject(f.toString());
                      }  
                   } 

                   else if (currMsg.compareTo("Quit") == 0)  {
                       ok = false;
                       removeClient(myID, myName);
		   			}
           
               }  // end synchronized                     
             }
             catch (ClassNotFoundException ex) {
                System.out.println("Client closing");
                ok = false;
            }
             	
            catch (IOException e)  {
                System.out.println("Client closing");
                ok = false;
            }
         }
      }
   }
}



