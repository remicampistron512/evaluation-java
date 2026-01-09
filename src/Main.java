import coursesshop.business.CourseBuyingService;
import coursesshop.ui.ConsoleMenus;
import java.sql.SQLException;

void main() throws SQLException {
    var service = new CourseBuyingService();
    var menus = new ConsoleMenus(service);
    menus.run();
}