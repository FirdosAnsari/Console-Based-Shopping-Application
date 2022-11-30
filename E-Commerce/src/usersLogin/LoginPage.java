package usersLogin;

import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
//import java.util.*;

public class LoginPage {
	static Connection conn = null;
	// static Scanner sc=new Scanner(System.in);
	static BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
	static PreparedStatement ps = null;
	static PreparedStatement ps1 = null;
	static PreparedStatement ps3 = null;
	static ResultSet rs = null;
	static String name = null;
	static String address = null;
	static String dob = null;
	static String password = null;
	static String userName = null;
	static String userPass = null;
	static Statement st = null;
	static String productName = null;
	static int productPrice = 0;
	static String productDescription = null;
	static int productId = 0;

	/*
	 * BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
	 * System.out.println("Enter your name"); String name = s.readLine();
	 * System.out.println("Enter your age"); int age =
	 * Integer.valueOf(s.readLine()); System.out.println(name + " " + age);
	 */
	public static void connectDb() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:orcl";
			String username = "system";
			String password = "Manager_1";
			// System.out.println("Connected");
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void disconnectDb() {
		try {
			conn.close();
			// System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void signUp() {
		try {
			connectDb();
			System.out.println("Enter Your Name : ");
			name = sc.readLine();
			System.out.println("Enter Your Address: ");
			address = sc.readLine();
			System.out.println("Date Format --> 'dd-mm-yy' : ");
			System.out.println("Enter Your Date Of Birth : ");
			dob = sc.readLine();
			System.out.println("Enter Password : ");
			password = sc.readLine();

			ps = conn.prepareStatement("insert into signup (name,address,dob,password) values(?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, address);
			ps.setString(3, dob);
			ps.setString(4, password);
			int val = ps.executeUpdate();
			if (val > 0) {
				System.out.println("Registering...");
				Thread.sleep(4000);
				System.out.println("Registered Successfully...");
				// disconnectDb();
			} else {
				System.out.println("Not Registered");
				disconnectDb();
			}
		} // try
		catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void userLogin() {

//String userName1;
		try {
			connectDb();
			System.out.print("Enter UserName : ");
			userName = sc.readLine();
			String userName2 = userName;
			System.out.print("Enter Password : ");
			userPass = sc.readLine();

			String userPass2 = userPass;
			if (userName.equals("admin") && userPass.equals("admin@123")) {
				System.out.println("Successfully Logged-in");
				adminUser();
			}
			userName = "'" + userName + "'";
			userPass = "'" + userPass + "'";
			if (conn != null)
				st = conn.createStatement();
			String query = "select count(*) from signup where name=" + userName + "and password=" + userPass;
			// System.out.println(query);
			if (st != null) {
				rs = st.executeQuery(query);
				if (rs != null) {
					int count = 0;
					// System.out.println(count);
					rs.next();
					count = rs.getInt(1);
					if (count != 0) {
						if (!userName2.equals("admin") && !userPass2.equals("admin@123"))
							System.out.println("Successfully Logged-in");
						if (userName2.equals("admin") && userPass2.equals("admin@123")) {

							// checkProduct();
							// System.out.println("Successfully Logged-out");
						} else {
							checkProduct();
							addCart();
						}

					} else {
						System.out.println("UserName and Password is not correct");

					}

				}

			}

			disconnectDb();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public static void checkProduct() {
		try {
			connectDb();
			Statement ps2 = conn.createStatement();
			ResultSet rs = ps2.executeQuery("select productId,productName,price,description from product");
			// Retrieving the ResultSetMetadata object
			ResultSetMetaData rsMetaData = rs.getMetaData();
			// System.out.println("List of column names in the current table: ");
			// Retrieving the list of column names
			int count = rsMetaData.getColumnCount();
			for (int i = 1; i <= count; i++) {
				System.out.print(rsMetaData.getColumnName(i) + "\t\t");
			}

			rs = ps2.executeQuery("select productId,productName,price,description from product");
			System.out.println(
					"\n-------------------------------------------------------------------------------------------");
			while (rs.next()) {
				System.out.println(
						rs.getInt(1) + "\t\t\t" + rs.getString(2) + "\t\t\t" + rs.getInt(3) + "\t\t" + rs.getString(4));
				System.out.println(
						"===========================================================================================");
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static void addCart() {
		try {
			String cart = null;
			String cart2 = null;
			String buy = null;
			int pId = 0;
			int count = 0;
			int sumCount = 0;
			connectDb();
			// if(!userName.equals("admin") && !userPass.equals("admin@123"))

			System.out.print("would you like to add the product in Cart...Yes/No : ");
			cart = sc.readLine();
			/*
			 * if(!cart.equals("yes") && !cart.equals("no")) {
			 * System.out.println("Please provide proper input 'yes/no' only"); //break;
			 * System.out.print("would you like to add the product in Cart...Yes/No : ");
			 * cart=sc.next(); }
			 */
			if (cart.equals("yes")) {
				while (true) {
					System.out.print("Enter ProductId  No : ");
					pId = Integer.valueOf(sc.readLine());
					// connectDb();
					ps3 = conn.prepareStatement("insert into cart select * from product where productId=?");
					ps3.setInt(1, pId);
					int val = ps3.executeUpdate();
					if (val > 0) {
						System.out.println("Added..");
					} else {
						System.out.println("Not Added");
						System.out.println("Please choose correct product ID");
					}
					System.out.print("Would you like to add more products..Yes/No : ");
					cart2 = sc.readLine();
					/*
					 * if(!cart2.equals("yes") && !cart2.equals("no")) {
					 * System.out.println("Please provide proper input 'yes/no' only"); //break;
					 * System.out.print("Would you like to add more products..Yes/No : ");
					 * cart=sc.next(); }
					 */
					++count;
					sumCount = count;
					// cart2=sc.next();
					if (cart2.equals("no")) {
						System.out.println(sumCount + " Product added into a Cart");
						break;
					}
				}
			}
			// else {
			// System.out.println("Please enter given option only...");
			if (cart.equals("yes")) {
				System.out.println("Would like to Buy the products..Yes/No : ");
				buy = sc.readLine();
				if (buy.equals("no")) {
					ps3 = conn.prepareStatement("delete from cart");
					// ps.setInt(1, productId);

					int val = ps3.executeUpdate();
					/*
					 * if (val>0) System.out.println("Deleted"); else
					 * System.out.println("Not Deleted");
					 */
					// Thread.sleep(2000);
					System.out.println("Please Visit Again...");
					System.out.println("You are Successfully logged-out.");
				}
				if (buy.equals("yes")) {
					Statement ps2 = conn.createStatement();
					rs = ps2.executeQuery("select productId,productName,price from cart");
					System.out.println("--------------------------------------------------------");
					while (rs.next()) {
						System.out.println(rs.getInt(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getInt(3));
						System.out.println("=========================================================");
					}
					rs = ps2.executeQuery("select sum(price) from cart");
					rs.next();
					// System.out.println(rs.getInt(1));
					int amount = rs.getInt(1);
					System.out.println("Total is :" + amount);
					while (true) {
						System.out.println("Enter the Amount : ");
						int amount2 = Integer.valueOf(sc.readLine());
						int sumAmount = 0;
						// int sumAmount2=sumAmount;
						if (amount == amount2) {
							sumAmount = amount - amount2;
							System.out.println(" Processing for Payment.... ");
							Thread.sleep(5000);
							System.out.println(" Payment Done Successfully. ");
							Thread.sleep(2000);
							System.out.println(" Balance is : " + sumAmount);
							Thread.sleep(2000);
							System.out.println(" Please Visit Again... ");
							System.out.println(" THANK YOU FOR SHOPPING ");
							Thread.sleep(1000);
							System.out.println(" Logged-out Successfully...");
							ps3 = conn.prepareStatement("delete from cart");
							ps3.executeUpdate();
							break;
						} else
							// if(amount2==0 && amount2>amount && amount2<0)
							// System.out.println("Amount is not correct ");
							// else
							System.out.println("Please Enter correct Amount ");
					} // while
				}
			}

			else {
				Thread.sleep(2000);
				System.out.println("Please Visit Again...");
			}
			while (cart.equals("no")) {
				Thread.sleep(1000);
				System.out.println("You are Successfully logged-out.");
				break;
			}

			// if(!cart.equals("yes") && !cart.equals("no")) {
			// System.out.println("Please provide proper input 'yes/no' only");
			// }

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 
	 */
	/*
	 * public static void userLogin1() { try { connectDb();
	 * System.out.print("Enter UserName : "); userName= sc.next();
	 * //userName="'"+userName+"'"; System.out.print("Enter Password : "); userPass
	 * = sc.next(); //userPass="'"+userPass+"'"; ps =
	 * conn.prepareStatement("select * from signup where name=? and password=?");
	 * ps.setString(1, name); ps.setString(2,password); int val =
	 * ps.executeUpdate(); if (val > 0) {
	 * System.out.println("UserName and Password is not correct"); } else {
	 * System.out.println("Successfully Logged-in"); disconnectDb(); } }
	 * catch(Exception e) { e.printStackTrace();
	 * 
	 * } }
	 */

	public static void adminUser() {
		try {
			while (true) {
				// System.out.println("Enter");
				System.out.print(" 1]Add.\n 2]Update.\n 3]Delete. \n 4]Logout. \n 5]Products Availability. \n");
				System.out.println("Enter Your Choice : ");
				int choice = Integer.valueOf(sc.readLine());
				if (choice == 4) {
					System.out.println("You are Successfully Logged-out..  ");
					break;
				}
				connectDb();
				if (choice == 1) {
					System.out.println("Add product Name : ");
					productName = sc.readLine();
					System.out.println("Add product Desciption : ");
					// productDescription = sc.nextLine();
					productDescription = sc.readLine();
					// String productName1="'"+productName+"'";
					System.out.println("Add product Price: ");
					productPrice = Integer.valueOf(sc.readLine());
					ps1 = conn.prepareStatement("insert into product (productName,description,price) values(?,?,?)");
					ps1.setString(1, productName);
					ps1.setString(2, productDescription);
					ps1.setInt(3, productPrice);
					int val = ps1.executeUpdate();
					if (val > 0)
						System.out.println("Product added");
					else
						System.out.println("Not Added");

				} // if
				else if (choice == 2) {
					System.out.print("Enter ProductId of the Product to be updated: ");
					productId = Integer.valueOf(sc.readLine());
					System.out.println("What do you want to update?");
					System.out.println("1. ProductName");
					System.out.println("2. Description");
					System.out.println("3. Price");
					System.out.println("4. Exit");
					System.out.print("Enter Your Choice: ");
					int updatedValue = Integer.valueOf(sc.readLine());
					// if()
					// while (true) {
					if (updatedValue == 4) {
						System.out.println("You are Successfully Exited.. ");
						break;
					} else if (updatedValue == 1) {
						// connectDb();
						System.out.print("Enter New ProductName: ");
						productName = sc.readLine();
						ps1 = conn.prepareStatement("update product set productName=? where productId=?");
						ps1.setString(1, productName);
						ps1.setInt(2, productId);
						System.out.println("ProductName: " + productName);
						System.out.println("ProductNameId: " + productId);
						int val = ps1.executeUpdate();
						if (val > 0)
							System.out.println("Updated");
						else
							System.out.println("Not Updated");
					} else if (updatedValue == 2) {
						// connectDb();
						System.out.print("Enter New Product Description: ");
						productDescription = sc.readLine();
						ps1 = conn.prepareStatement("update product set description=? where productId=?");
						ps1.setString(1, productDescription);
						ps1.setInt(2, productId);
						int val = ps1.executeUpdate();
						if (val > 0)
							System.out.println("Updated");
						else
							System.out.println("Not Updated");
					} else if (updatedValue == 3) {
						// connectDb();
						System.out.print("Enter New Product Price: ");
						productPrice = Integer.valueOf(sc.readLine());
						ps1 = conn.prepareStatement("update product set price=? where productId=?");
						ps1.setInt(1, productPrice);
						ps1.setInt(2, productId);
						int val = ps1.executeUpdate();
						if (val > 0)
							System.out.println("Updated");
						else
							System.out.println("Not Updated");
					} else
						System.out.print("Invalid Choice");
					// } //while

				} // else-if
				else if (choice == 3) {
					// connectDb();
					System.out.print("Enter ProductId of the Product to be deleted: ");
					productId = Integer.valueOf(sc.readLine());
					ps = conn.prepareStatement("delete from product where productId=?");
					ps.setInt(1, productId);

					int val = ps.executeUpdate();
					if (val > 0)
						System.out.println("Deleted");
					else
						System.out.println("Not Deleted");
				} else if (choice == 5) {
					checkProduct();
				} else
					System.out.print("Invalid Choice");

			} // while
		} // try
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			System.out.println("Welcome to BIg BaZar ");
			while (true) {
				System.out.println("Please Choose below option.\n 1] Sign Up.\n 2]User-Login.\n 3]Exit.");
				System.out.print("Enter Your Choice: ");
				int choi = Integer.valueOf(sc.readLine());
				if (choi == 3) {
					System.out.print("You are Successfully Exited..  ");
					break;
				} else if (choi == 1) {
					signUp();

				} else if (choi == 2) {
					userLogin();
				} else {
					System.out.println("Invalid Choice");
					// disconnectDb();
				}

			} // while
		} // try
		catch (Exception e) {
			e.printStackTrace();
		}
		// connectDb();
		// disconnectDb();
	}
}

