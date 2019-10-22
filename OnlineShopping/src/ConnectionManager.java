import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class ConnectionManager {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException  {



		Class.forName("com.mysql.jdbc.Driver");  
		Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/shopping","root","");  

		if (con != null)
		{
			System.out.println("Connected");
		}
		else
		{
			System.out.println("not Connected");
		}


		boolean i=false;
		do {


			System.out.println("1.Admin Login");
			System.out.println("2.Agent Login");
			System.out.println("3.Exit");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			int choice = Integer.parseInt(br.readLine());
			Statement statement = (Statement) con.createStatement();

			switch(choice)
			{
			case 1:
				System.out.print("Username : ");
				String user = br.readLine();
				System.out.print("Password : ");
				String pass = br.readLine();

				boolean j=true;
				do {

					ResultSet res=statement.executeQuery("select Username,Password from admin where Username='"+user+"' and password ='"+pass+"'");
					if(res.next())
					{
						System.out.println(" \n              Admin page        ");
						System.out.println("\n1.Add Product");
						System.out.println("2.Display Inventory Details");
						System.out.println("3.Logout");

						int ch = Integer.parseInt(br.readLine());

						switch(ch)
						{
						case 1:

							System.out.print("Product Id :");
							int pId=Integer.parseInt(br.readLine());
							System.out.print("Product Name : ");
							String pName=br.readLine();
							System.out.print("Min Sell Quantity Price: ");
							int min=Integer.parseInt(br.readLine());
							System.out.print("Price: ");
							int price=Integer.parseInt(br.readLine());

							statement.executeUpdate("INSERT INTO addproduct (Id,Product_name,Min_sell,price) values('"+pId+"','"+pName+"','"+min+"','"+price+"')");


							break;

						case 2:
							int value=0;
							System.out.print("Product Id :");
							int pId1=Integer.parseInt(br.readLine());
							System.out.print("Quantity Available : ");
							int qnty=Integer.parseInt(br.readLine());
							statement.executeUpdate("update addproduct SET quantity_available ='"+qnty+"'where Id='"+pId1+"'");
							ResultSet rs=statement.executeQuery("select price from addproduct");
							if(rs.next())
							{
								value=rs.getInt(1);

							}
							int price1=value*qnty;
							System.out.println("Total cost : "+price1);

							statement.executeUpdate("update addproduct SET total_cost ='"+price1+"'where Id='"+pId1+"'and quantity_available='"+qnty+"'");


							ResultSet rs1=statement.executeQuery("select Id,product_name,min_sell,price,quantity_available,total_cost from addproduct where Id='"+pId1+"'");
							while (rs1.next()) {
								int id = rs1.getInt(1);
								String name = rs1.getString(2);
								int  quantity = rs1.getInt(5);
								int pr = rs1.getInt(4);
           int tot = rs1.getInt(6);
           System.out.println("Product Id : "+id+"\nproduct name : "+name+"\nQuantity available : "+quantity+"\nPrice : "+pr+"\nTotal cost : "+tot);
							}
							break;


						case 3:
							j=false;


						}	


					}
					else {
						System.out.println("Username and password are not matching");
					}
				}while(j);
				i=true;

				break;


			case 2:

				System.out.println("    Agent Login");

				System.out.print("Username : ");
				String user1 = br.readLine();
				System.out.print("Password : ");
				String pass1 = br.readLine();

				boolean g=true;
				do {
					ResultSet res1=statement.executeQuery("select User,Pass from agent where User='"+user1+"' and pass ='"+pass1+"'");
					if(res1.next())
					{
						System.out.println("1.Buy/sell");
						System.out.println("2.Show history");
						System.out.println("3.Logout");

						int ch = Integer.parseInt(br.readLine());

switch(ch) {

case 1:

System.out.print("Enter product Id : ");
int prId = Integer.parseInt(br.readLine());
System.out.println("Buy/Sell");
String b =br.readLine();
if(b.equalsIgnoreCase("buy"))
{
ResultSet rs1=statement.executeQuery("select Id,product_name from addproduct where Id='"+prId+"'");
while (rs1.next()) {
           int id = rs1.getInt(1);
           String name = rs1.getString(2);
           System.out.println("Product Id : "+id+"\nproduct name : "+name+" ");
}
System.out.print("Enter quantity : ");
int qnty=Integer.parseInt(br.readLine());

ResultSet rs2=statement.executeQuery("select price,quantity_available from addproduct where Id='"+prId+"'");
int qn=0,pr=0;
while (rs2.next()) {

           pr = rs2.getInt(1);
           qn = rs2.getInt(2);
           //System.out.println("Price  : "+pr+"\nqn : "+qn+"");
}
if(qnty<=qn)
{
int tc = qnty*pr;
System.out.println("Total cost for your quantity is :"+tc);

System.out.print("Do you want to confirm ? yes/no : ");
String y =br.readLine();
if(y.equalsIgnoreCase("Yes"))
{
ResultSet rs4=statement.executeQuery("select quantity_available,price,product_name from addproduct where Id='"+prId+"'");
int qn1=0,pr3=0;
String pName="";
while (rs4.next()) {


           qn1 = rs4.getInt(1);
           pr3 = rs4.getInt(2);
           pName = rs4.getString(3);
}
int qt=qn1-qnty;
int tot1=qt*pr3;
System.out.println(tot1);
System.out.println(qt);
statement.executeUpdate("update addproduct SET total_cost ='"+tot1+"'where Id='"+prId+"'");
statement.executeUpdate("update addproduct SET quantity_available ='"+qt+"'where Id='"+prId+"'");
statement.executeUpdate("INSERT INTO history values('"+prId+"','"+pName+"','"+"Buy"+"','"+qt+"','"+pr3+"','"+tot1+"','"+user1+"')");

g=false;
}
else
g=true;

}


else
System.out.println("Product not available");
}
if(b.equalsIgnoreCase("sell"))
{
System.out.println("Sell");


System.out.print("Quantity : ");
int quantity = Integer.parseInt(br.readLine());
ResultSet rs3=statement.executeQuery("select quantity_available,price,product_name from addproduct where Id='"+prId+"'");
int qnt=0,pr3=0,tot=0;
String prd="";
while (rs3.next()) {

         
           qnt = rs3.getInt(1);
           pr3 = rs3.getInt(2);
           prd = rs3.getString(3);
         
}

int quantity1=quantity+qnt;
tot=quantity1*pr3;
System.out.println(quantity1);
statement.executeUpdate("update addproduct SET toatal_cost ='"+tot+"'where Id='"+prId+"'");
statement.executeUpdate("update addproduct SET quantity_available ='"+quantity1+"'where Id='"+prId+"'");
statement.executeUpdate("INSERT INTO history values('"+prId+"','"+prd+"','"+"sell"+"','"+quantity1+"','"+pr3+"','"+tot+"','"+user1+"')");
break;
}
case 2:

ResultSet rs5=statement.executeQuery("select Id,product_name,Transaction,quantity,cost,total_cost from history where Username='"+user1+"'");
while (rs5.next()) {
           int id = rs5.getInt(1);
           String name = rs5.getString(2);
           String  trans = rs5.getString(3);
           int quanty = rs5.getInt(4);
           int cost = rs5.getInt(5);
           int total_cost=rs5.getInt(6);
           System.out.println("Product Id : "+id+"\nproduct name : "+name+"\nTranaction : "+trans+"\nQuantity : "+quanty+"\nCost : "+cost+"\nTotal cost : "+total_cost);
}


case 3:
g=false;
}

}



else
System.out.println("Username and password are not matching");
break;

}while(g);

i=true;


case 3:
System.out.println("     Thanks for shopping:)       ");
i=false;



}



}while(i);



}


}

