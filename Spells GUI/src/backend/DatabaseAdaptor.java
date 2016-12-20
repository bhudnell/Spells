package backend;

import java.sql.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;

public class DatabaseAdaptor {

	Connection con;

	public DatabaseAdaptor() {
		try {
			String host = "jdbc:mariadb://localhost:3306/spells?useUnicode=true&amp;characterEncoding=UTF-8";
			String user = "root";
			String password = "";

			con = DriverManager.getConnection(host, user, password);
		} catch (SQLException err) {
			System.out.println(err.getMessage());
			System.exit(1);
		}

	}

	public void createTable() {
		try {
			CSVReader in = new CSVReader(new FileReader("./src/backend/spell_full.csv"));

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
						SQL += "`" + cols[i] + "` TEXT(60000) CHARACTER SET utf8mb4";
					}
				}

			}
			SQL += ", PRIMARY KEY (`id`));";

			PreparedStatement stmt = this.con.prepareStatement(SQL);
			stmt.executeQuery();

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
			in = new CSVReader(new FileReader("./src/backend/spell_full.csv"));

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
						str = str.replace("\\xFFFD", "");
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
		String SQL = "DROP TABLE spells;";
		PreparedStatement stmt;
		try {
			stmt = this.con.prepareStatement(SQL);
			stmt.executeQuery();

			createTable();
			addValuesToTable();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public void nameSearch(String search) {
		PreparedStatement stmt;
		String str = "%" + search + "%";
		try {
			String SQL = "SELECT name, description FROM spells WHERE name LIKE ?";

			stmt = this.con.prepareStatement(SQL);
			stmt.setString(1, str);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("name");
				String descrip = rs.getString("description");

				System.out.println(name + "\n" + descrip);
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}