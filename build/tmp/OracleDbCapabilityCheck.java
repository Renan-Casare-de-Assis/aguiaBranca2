import java.sql.*;
public class OracleDbCapabilityCheck {
    private static final String URL = "jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL";
    private static final String USER = "RM563592";
    private static final String PASS = "070589";
    public static void main(String[] args) {
        System.out.println("== Oracle DB Capability Check ==");
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (Throwable t) {
            System.out.println("[FAIL] Driver load: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            return;
        }
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);
            System.out.println("[OK] Connection established");
            runSelectCheck(conn, "USUARIOS", "SELECT COUNT(*) FROM USUARIOS");
            runSelectCheck(conn, "IDEIAS", "SELECT COUNT(*) FROM IDEIAS");
            runSelectCheck(conn, "PROJETOS", "SELECT COUNT(*) FROM PROJETOS");
            runSelectCheck(conn, "DIRETRIZES", "SELECT COUNT(*) FROM DIRETRIZES");
            runWriteCapability(conn, "IDEIAS",
                "INSERT INTO IDEIAS (ID, TITULO, DESCRICAO, CATEGORIA, OPERADOR_ID, OPERADOR_NOME, UNIDADE, STATUS, CRIADO_EM, ATUALIZADO_EM) SELECT ID, TITULO, DESCRICAO, CATEGORIA, OPERADOR_ID, OPERADOR_NOME, UNIDADE, STATUS, CRIADO_EM, ATUALIZADO_EM FROM IDEIAS WHERE 1=0",
                "UPDATE IDEIAS SET ATUALIZADO_EM = ATUALIZADO_EM WHERE 1=0",
                "DELETE FROM IDEIAS WHERE 1=0"
            );
            runWriteCapability(conn, "PROJETOS",
                "INSERT INTO PROJETOS (ID, IDEIA_ID, TITULO, OBJETIVO, ETAPA, STATUS, GERENTE_ID, DATA_INICIO, DATA_FIM_META, INVESTIMENTO, CRIADO_EM, ATUALIZADO_EM) SELECT ID, IDEIA_ID, TITULO, OBJETIVO, ETAPA, STATUS, GERENTE_ID, DATA_INICIO, DATA_FIM_META, INVESTIMENTO, CRIADO_EM, ATUALIZADO_EM FROM PROJETOS WHERE 1=0",
                "UPDATE PROJETOS SET ATUALIZADO_EM = ATUALIZADO_EM WHERE 1=0",
                "DELETE FROM PROJETOS WHERE 1=0"
            );
            runWriteCapability(conn, "DIRETRIZES",
                "INSERT INTO DIRETRIZES (ID, TITULO, DESCRICAO, PILAR, STATUS, CRIADO_POR, CRIADO_EM, ATUALIZADO_EM) SELECT ID, TITULO, DESCRICAO, PILAR, STATUS, CRIADO_POR, CRIADO_EM, ATUALIZADO_EM FROM DIRETRIZES WHERE 1=0",
                "UPDATE DIRETRIZES SET ATUALIZADO_EM = ATUALIZADO_EM WHERE 1=0",
                "DELETE FROM DIRETRIZES WHERE 1=0"
            );
            conn.rollback();
            System.out.println("[OK] Rollback executed (no persistent changes)");
        } catch (Throwable t) {
            System.out.println("[FAIL] Connection/session: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            t.printStackTrace(System.out);
        }
    }
    private static void runSelectCheck(Connection conn, String name, String sql) {
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            rs.next();
            int count = rs.getInt(1);
            System.out.println("[OK] SELECT " + name + " count=" + count);
        } catch (Throwable t) {
            System.out.println("[FAIL] SELECT " + name + ": " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }
    private static void runWriteCapability(Connection conn, String table, String insertSql, String updateSql, String deleteSql) {
        runDml(conn, "INSERT", table, insertSql);
        runDml(conn, "UPDATE", table, updateSql);
        runDml(conn, "DELETE", table, deleteSql);
    }
    private static void runDml(Connection conn, String op, String table, String sql) {
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            int affected = st.executeUpdate();
            System.out.println("[OK] " + op + " capability on " + table + " (affected=" + affected + ")");
        } catch (Throwable t) {
            System.out.println("[FAIL] " + op + " capability on " + table + ": " + t.getClass().getSimpleName() + " - " + t.getMessage());
        }
    }
}
