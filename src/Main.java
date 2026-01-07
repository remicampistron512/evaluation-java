import coursesshop.business.CourseBuyingService;
import coursesshop.ui.ConsoleMenus;

void main() {
    var service = new CourseBuyingService();
    var menus = new ConsoleMenus(service);
    menus.run();
}