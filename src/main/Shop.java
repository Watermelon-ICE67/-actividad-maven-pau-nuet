package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;
import model.Employee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Shop {
	private static final Scanner CONSOLE = new Scanner(System.in);
	private Amount cash = new Amount(100.00);
//	private Product[] inventory;
	private ArrayList<Product> inventory;
	private int numberProducts;
//	private Sale[] sales;
	private ArrayList<Sale> sales;
	private int numberSales;

	final static double TAX_RATE = 1.04;
	private static final String ITEMS_FILE = "items.txt";

	public Shop() {
		inventory = new ArrayList<Product>();
		sales = new ArrayList<Sale>();
	}
	
	

	public Amount getCash() {
		return cash;
	}



	public void setCash(Amount cash) {
		this.cash = cash;
	}



	public ArrayList<Product> getInventory() {
		return inventory;
	}



	public void setInventory(ArrayList<Product> inventory) {
		this.inventory = inventory;
	}



	public int getNumberProducts() {
		return numberProducts;
	}



	public void setNumberProducts(int numberProducts) {
		this.numberProducts = numberProducts;
	}



	public ArrayList<Sale> getSales() {
		return sales;
	}



	public void setSales(ArrayList<Sale> sales) {
		this.sales = sales;
	}



	public int getNumberSales() {
		return numberSales;
	}



	public void setNumberSales(int numberSales) {
		this.numberSales = numberSales;
	}



	public static void main(String[] args) {
		Shop shop = new Shop();

		// load inventory from external data
		shop.loadInventory();
		
		// init session as employee
		shop.initSession();

		Scanner scanner = CONSOLE;
		int opcion = 0;
		boolean exit = false;

		do {
			System.out.println("\n");
			System.out.println("===========================");
			System.out.println("Menu principal ComputerCenter");
			System.out.println("===========================");
			System.out.println("1) Comptar caixa");
			System.out.println("2) Afegir article");
			System.out.println("3) Afegir estoc");
			System.out.println("4) Marcar article amb descompte");
			System.out.println("5) Veure inventari");
			System.out.println("6) Venda");
			System.out.println("7) Veure vendes");
			System.out.println("8) Veure venda total");
			System.out.println("9) Eliminar article");
			System.out.println("10) Desar inventari");
			System.out.println("11) Sortir del programa");
			System.out.print("Seleccioneu una opció: ");
			opcion = scanner.nextInt();

			switch (opcion) {
			case 1:
				shop.showCash();
				break;

			case 2:
				shop.addProduct();
				break;

			case 3:
				shop.addStock();
				break;

			case 4:
				shop.setExpired();
				break;

			case 5:
				shop.showInventory();
				break;

			case 6:
				shop.sale();
				break;

			case 7:
				shop.showSales();
				break;

			case 8:
				shop.showSalesAmount();
				break;

			case 9:
				shop.removeProduct();
				break;

			case 10:
				shop.saveInventory();
				break;

			case 11:
				System.out.println("Tancant el programa ...");
				exit = true;
				break;
			}

		} while (!exit);

	}

	private void initSession() {
		// TODO Auto-generated method stub
		
		Employee employee = new Employee("test");
		boolean logged=false;
		
		do {
			Scanner scanner = CONSOLE;
			System.out.println("Introduïu número d'empleat: ");
			int employeeId = scanner.nextInt();
			
			System.out.println("Introduïu contrasenya: ");
			String password = scanner.next();
			
			logged = employee.login(employeeId, password);
			if (logged) {
				System.out.println("Login correcte ");
			} else {
				System.out.println("Usuari o contrasenya incorrectes ");
			}
		} while (!logged);
				
	}

	/**
	 * load initial inventory to shop
	 */
	public void loadInventory() {
//		addProduct(new Product("Manzana", new Amount(10.00), true, 10));
//		addProduct(new Product("Pera", new Amount(20.00), true, 20));
//		addProduct(new Product("Hamburguesa", new Amount(30.00), true, 30));
//		addProduct(new Product("Fresa", new Amount(5.00), true, 20));
		// now read from file
		this.readInventory();
	}

	/**
	 * read inventory from file
	 */
	private void readInventory() {
		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + ITEMS_FILE);

		try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
			String line;
			int lineNumber = 0;
			while ((line = bf.readLine()) != null) {
				lineNumber++;

				// les línies en blanc s'ignoren sense avisar (no són dades)
				if (line.isBlank()) {
					continue;
				}

				Product product = parseProductLine(line);
				if (product != null) {
					addProduct(product);
				} else {
					System.out.println("Aviso: la línia " + lineNumber + " d'items.txt no és vàlida i s'ignora: " + line);
				}
			}
		} catch (IOException e) {
			// TODO RA1-ISSUE-11: missatge comprensible per fitxer absent o error de lectura.
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Convert a valid line from items.txt into a Product.
	 * * Format: Item:<nom>;Wholesaler Price:<preu>;Stock:<estoc>;
	 * 
	 * @return the Product, or null if the line is not valid
	 */
	private Product parseProductLine(String line) {
		if (line == null || line.isBlank()) {
			return null;
		}

		String name = null;
		Double price = null;
		Integer stock = null;

		// separa camps (;) i, dins de cada camp, clau:valor
		for (String field : line.split(";")) {
			String[] keyValue = field.split(":", 2);
			if (keyValue.length != 2) {
				continue;
			}
			String key = keyValue[0].trim();
			String value = keyValue[1].trim();

			try {
				switch (key) {
				case "Item":
					name = value;
					break;
				case "Wholesaler Price":
					price = Double.parseDouble(value);
					break;
				case "Stock":
					stock = Integer.parseInt(value);
					break;
				default:
					break;
				}
			} catch (NumberFormatException e) {
				// valor numèric no vàlid: la línia no genera Product
				return null;
			}
		}
		

		if (name == null || name.isEmpty() || price == null || stock == null) {
			return null;
		}
		if (name == null || name.isEmpty() || price == null || stock == null) {
			return null;
		}
		if (price < 0 || stock < 0) {
			return null;
		}

		return new Product(name, new Amount(price), stock > 0, stock);

	}

	
	
	/**
	 * Save the current inventory to items.txt.
	 */
	public void saveInventory() {
		// TODO RA1-ISSUE-09: convertir cada Product en una linia i reescriure items.txt.
		System.out.println("RA1-ISSUE-09 pendent: encara no s'ha desat l'inventari.");
	}

	/**
	 * show current total cash
	 */
	private void showCash() {
		System.out.println("Diners actuals: " + cash);
	}

	/**
	 * add a new product to inventory getting data from console
	 */
	public void addProduct() {
		if (isInventoryFull()) {
			System.out.println("No es poden afegir més articles, s'ha assolit el màxim.");
			return;
		}
		
		Scanner scanner = CONSOLE;

		// el menú fa servir nextInt(), així que pot quedar un salt de línia pendent
		System.out.print("Nom de l'article: ");
		String name = scanner.nextLine().trim();
		if (name.isEmpty()) {
			name = scanner.nextLine().trim();
		}

		// validacions de dades mínimes
		if (name.isEmpty()) {
			System.out.println("El nom no pot estar buit. No s'afegeix l'article.");
			return;
		}
		if (name.contains(";") || name.contains(":")) {
			System.out.println("El nom no pot contenir ';' ni ':'. No s'afegeix l'article.");
			return;
		}
		if (findProduct(name) != null) {
			System.out.println("Ja existeix un article amb aquest nom. No s'afegeix l'article.");
			return;
		}

		double wholesalerPrice;
		int stock;
		try {
			System.out.print("Preu Majorista: ");
			wholesalerPrice = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
			System.out.print("Estoc: ");
			stock = Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("El preu o l'estoc no són numèrics. No s'afegeix l'article.");
			return;
		}

		if (wholesalerPrice <= 0 || stock < 0) {
			System.out.println("El preu ha de ser positiu i l'estoc no pot ser negatiu. No s'afegeix l'article.");
			return;
		}

		Product product = new Product(name, new Amount(wholesalerPrice), stock > 0, stock);
		addProduct(product);
		System.out.println("Article afegit: " + product);
	}

	/**
	 * remove a new product to inventory getting data from console
	 */
	public void removeProduct() {
		Scanner scanner = CONSOLE;
		
		System.out.print("Seleccioneu un nom d'article: ");
		String name = scanner.nextLine().trim();
		if (name.isEmpty()) {
			name =scanner.nextLine().trim();
			}
		Product product = findProduct(name);
		if(product == null) {
			System.out.println("No s'ha trobat cap article amb " + name);
			return;
		}
		
		inventory.remove(product);
		numberProducts--;
		
		System.out.println("El producte " + name + " ha sigut eliminat correctament"); 
		
	}

	/**
	 * add stock for a specific product
	 */
	public void addStock() {
		Scanner scanner = CONSOLE;
		
		System.out.print("Seleccioneu un nom d'article: ");
		String name = scanner.nextLine().trim();
		if(name.isEmpty()); {
			name = scanner.nextLine().trim();
		}
		
		Product product =findProduct(name);
		if (product == null) {
			System.out.println("No s'ha trobat l'article amb nom " + name);
			return;
		}
		
		int quantity;
		try {
			System.out.print("Seleccioneu la quantitat a afegir: ");
			quantity = Integer.parseInt(scanner.nextLine().trim());
			
		} catch (NumberFormatException e) {
			System.out.println("La quantitat no es numèrica. No es canvia l'estoc.");
			return;
		}
		
		if (quantity <= 0) {
			System.out.println("La quantitat ha de ser positiva. No es canvia l'estoc.");
			return;
		}
		
		//nomes canvia l'estoc.
		product.setStock(product.getStock() + quantity);
		
		// si el producte estava esgotat, torna a estar disponible.
		product.setAvailable(true);
		
		System.out.println("L¿estoc de l'article " + name + "s'ha actualitzat a " + product.getStock());
	
	}
	  

	/**
	 * set a product as expired
	 */
	private void setExpired() {
		Scanner scanner = CONSOLE;
		System.out.print("Seleccioneu un nom d'article: ");
		String name = scanner.next();

		Product product = findProduct(name);

		if (product != null) {
			product.expire();
			System.out.println("El preu de l'article " + name + " s'ha actualitzat a " + product.getPublicPrice());
		}
	}

	/**
	 * show all inventory
	 */
	public void showInventory() {
		System.out.println("Inventari actual de ComputerCenter:");
		for (Product product : inventory) {
			if (product != null) {
				System.out.println(product);
			}
		}
	}

	/**
	 * make a sale of products to a client
	 */
	public void sale() {
		// ask for client name
		Scanner sc = CONSOLE;
		System.out.println("Realitzar venda, escriviu el nom del client");
		String nameClient = sc.nextLine();
		Client client = new Client(nameClient);

		// sale product until input name is not 0
		// Product[] shoppingCart = new Product[10];
		ArrayList<Product> shoppingCart = new ArrayList<Product>();
		int numberShopping = 0;

		Amount totalAmount = new Amount(0.0);
		String name = "";
		while (!name.equals("0")) {
			System.out.println("Introduïu el nom de l'article, escriviu 0 per acabar:");
			name = sc.nextLine();

			if (name.equals("0")) {
				break;
			}
			Product product = findProduct(name);
			boolean productAvailable = false;

			if (product != null && product.isAvailable()) {
				productAvailable = true;
				totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());
				product.setStock(product.getStock() - 1);
				shoppingCart.add(product);
				numberShopping++;
				// if no more stock, set as not available to sale
				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				System.out.println("Article afegit correctament");
			}

			if (!productAvailable) {
				System.out.println("Article no trobat o sense estoc");
			}
		}

		totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
		// show cost total
		System.out.println("Venda realitzada correctament, total: " + totalAmount);
		
		// make payment
		if(!client.pay(totalAmount)) {
			System.out.println("El client deu: " + client.getBalance());;
		}

		// create sale
		Sale sale = new Sale(client, shoppingCart, totalAmount);

		// add to shop
		sales.add(sale);
//		numberSales++;

		// add to cash
		cash.setValue(cash.getValue() + totalAmount.getValue());
	}

	/**
	 * show all sales
	 */
	private void showSales() {
		System.out.println("Llista de vendes:");
		for (Sale sale : sales) {
			if (sale != null) {
				System.out.println(sale);
			}
		}
		
		// ask for client name
		Scanner sc = CONSOLE;
		System.out.println("Exportar fitxer de vendes? S / N");
		String option = sc.nextLine();
		if ("S".equalsIgnoreCase(option)) {
			this.writeSales();
		} 
		
	}

	/**
	 * write in file the sales done
	 */
	private void writeSales() {
		// define file name based on date
		LocalDate myObj = LocalDate.now();
		String fileName = "sales_" + myObj.toString() + ".txt";
		
		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);
				
		try {
			// wrap in proper classes
			FileWriter fw;
			fw = new FileWriter(f, true);
			PrintWriter pw = new PrintWriter(fw);
			
			// write line by line
			int counterSale=1;
			for (Sale sale : sales) {				
				// format first line TO BE -> 1;Client=PERE;Date=29-02-2024 12:49:50;
				StringBuilder firstLine = new StringBuilder(counterSale+";Client="+sale.getClient()+";Date=" + sale.formatDate()+";");
				pw.write(firstLine.toString());
				fw.write("\n");
				
				// format second line TO BE -> 1;Products=Manzana,20.0€;Fresa,10.0€;Hamburguesa,60.0€;
				// build products line
				StringBuilder productLine= new StringBuilder();
				for (Product product : sale.getProducts()) {
					productLine.append(product.getName()+ "," + product.getPublicPrice()+";");
				}
				StringBuilder secondLine = new StringBuilder(counterSale+ ";" + "Products=" + productLine +";");						                                                
				pw.write(secondLine.toString());	
				fw.write("\n");
				
				// format third line TO BE -> 1;Amount=93.60€;
				StringBuilder thirdLine = new StringBuilder(counterSale+ ";" + "Amount=" + sale.getAmount() +";");						                                                
				pw.write(thirdLine.toString());	
				fw.write("\n");
				
				// increment counter sales
				counterSale++;
			}
			// close files
			pw.close();
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**
	 * show total amount all sales
	 */
	private void showSalesAmount() {
		Amount totalAmount = new Amount(0.0);
		for (Sale sale : sales) {
			if (sale != null) {
				totalAmount.setValue(totalAmount.getValue() + sale.getAmount().getValue());
			}
		}
		System.out.println("Total de vendes:");
		System.out.println(totalAmount);
	}

	/**
	 * add a product to inventory
	 * 
	 * @param product
	 */
	public void addProduct(Product product) {
		if (isInventoryFull()) {
			System.out.println("No es poden afegir més articles, s'ha assolit el màxim de " + inventory.size());
			return;
		}
		inventory.add(product);
		numberProducts++;
	}
	
	

	/**
	 * check if inventory is full or not
	 */
	public boolean isInventoryFull() {
		if (numberProducts == 10) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * find product by name
	 * 
	 * @param product name
	 */
	public Product findProduct(String name) {
		for (int i = 0; i < inventory.size(); i++) {
			if (inventory.get(i) != null && inventory.get(i).getName().equalsIgnoreCase(name)) {
				return inventory.get(i);
			}
		}
		return null;

	}

}