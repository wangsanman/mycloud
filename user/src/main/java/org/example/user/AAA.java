package org.example.user;

public class AAA {
    private final int[] c;
    private int[] d;

    public AAA(int[] c) {
        this.c = c;
    }


    public void setC(int[] c) {
        Runnable runnable = () -> {
        };
        c[0] = c[0] + c[1];
//        try (Connection connection = DriverManager.getConnection(url, username, password);
//             Statement statement = connection.prepareStatement()) {
//
//            String query = "SELECT * FROM users";
//            ResultSet resultSet = statement.executeQuery(query);
//
//            while (resultSet.next()) {
//                Long id = resultSet.getLong("id");
//                String name = resultSet.getString("name");
//                System.out.println("User ID: " + id + ", Name: " + name);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }


    static {
        System.out.println("我是AAA");
    }
}
