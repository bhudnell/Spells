<<<<<<< HEAD:Spells GUI/src/DatabaseAdaptor.java
=======
//package backend;
>>>>>>> origin/master:Spells GUI/src/backend/DatabaseAdaptor.java

import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.opencsv.CSVReader;

public class DatabaseAdaptor {

	Connection con;

	public DatabaseAdaptor() {
		try {
			String host = "jdbc:sqlite:./src/spells.db";

			con = DriverManager.getConnection(host);

			// String host =
			// "jdbc:mariadb://localhost:3306/spells?useUnicode=true&amp;characterEncoding=UTF-8";
			// String user = "root";
			// String password = "";
			//
			// con = DriverManager.getConnection(host, user, password);
		} catch (SQLException err) {
			System.out.println(err.getMessage());
			System.exit(1);
		}

	}

	public void createTable() {
		try {
			CSVReader in = new CSVReader(new FileReader("./src/spell_full.csv"));

			String[] cols = in.readNext();
			String[] vars = in.readNext();
			in.close();

			String SQL = "CREATE TABLE IF NOT EXISTS spells (";
			for (int i = 0; i < cols.length; i++) {
				if (i != 0)
					SQL += ", ";

				try {
					Integer.parseInt(vars[i]);
					SQL += "`" + cols[i] + "` INT(20)";
				} catch (NumberFormatException e) {
					if (vars[i].equals("NULL")) {
						SQL += "`" + cols[i] + "` INT(20)";
					} else {
						SQL += "`" + cols[i] + "` TEXT(60000)"; // CHARACTER SET
																// utf8mb4";
					}
				}

			}
			SQL += ", PRIMARY KEY (`id`));";

			PreparedStatement stmt = this.con.prepareStatement(SQL);
			stmt.execute();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SQLException err) {
			System.out.println(err.getMessage());
		} catch (IOException error) {
			System.out.println(error.getMessage());
		}
	}

	public void addValuesToTable() {
		CSVReader in;
		String[] nextLine;
		PreparedStatement stmt = null;
		try {
			in = new CSVReader(new FileReader("./src/spell_full.csv"));

			String[] header = in.readNext();
			String questionMarks = "";
			for (int i = 0; i < header.length; i++) {
				if (i != 0)
					questionMarks += ",";
				questionMarks += "?";
			}

			String SQL = "INSERT INTO spells VALUES(" + questionMarks + ");";

			stmt = this.con.prepareStatement(SQL);

			final int batchSize = 1000;
			int count = 0;
			while ((nextLine = in.readNext()) != null) {
				if (null != nextLine) {
					int index = 1;
					for (String str : nextLine) {
						if (str.equals("NULL"))
							stmt.setString(index++, null);
						else
							stmt.setString(index++, str);
					}
					stmt.addBatch();
				}
				if (++count % batchSize == 0)
					stmt.executeBatch();
			}
			stmt.executeBatch();
			con.commit();

			in.close();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (SQLException err) {
			System.out.println(err.getMessage());
		} catch (IOException error) {
			System.out.println(error.getMessage());
		}
	}

	public void updateTable() {
		saveFile("https://spreadsheets.google.com/pub?key=0AhwDI9kFz9SddG5GNlY5bGNoS2VKVC11YXhMLTlDLUE&output=csv",
				"./src/spell_full.csv");

		String SQL = "DROP TABLE spells;";
		PreparedStatement stmt;
		try {
			stmt = this.con.prepareStatement(SQL);
			stmt.execute();

			createTable();
			addValuesToTable();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public ResultSet nameSearch(String query) {
		PreparedStatement stmt;
		String str = "%" + query + "%";
		ResultSet rs = null;
		try {
			String SQL = "SELECT * FROM spells WHERE name LIKE ?";

			stmt = this.con.prepareStatement(SQL);
			stmt.setString(1, str);

			rs = stmt.executeQuery();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return rs;
	}
<<<<<<< HEAD:Spells GUI/src/DatabaseAdaptor.java

	public void saveFile(String url, String file) {
		try {
			InputStream in = new URL(url).openStream();
			FileOutputStream fos = new FileOutputStream(new File(file));

			System.out.println("Reading file");
			int length = -1;
			byte[] buffer = new byte[1024];
			while ((length = in.read(buffer)) > -1)
				fos.write(buffer, 0, length);

			fos.close();
			in.close();
			System.out.println("Downloaded");

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

}
=======
}
>>>>>>> origin/master:Spells GUI/src/backend/DatabaseAdaptor.java
