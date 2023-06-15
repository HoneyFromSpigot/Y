package io.github.thewebcode.yplugin.inventory.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPage {

    public class Menu extends ItemMenu {
        private int page;

        public Menu(String title, int rows) {
            super(title, rows);
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }

    private List<MenuItem> pageItems;
    private int pageIndex;
    private String itemPageTitleFormat = "{title} - Page {pageIndex}";
    private String title;

    private Map<MenuAction, List<MenuBehaviour>> behaviours = new HashMap<>();

    public ItemPage(int pageIndex, List<MenuItem> items) {
        this.pageItems = items;
        this.pageIndex = pageIndex;
        behaviours.put(MenuAction.OPEN, new ArrayList<>());
        behaviours.put(MenuAction.CLOSE, new ArrayList<>());
    }

    public void addBehaviour(MenuAction action, MenuBehaviour behaviour) {
        behaviours.get(action).add(behaviour);
    }

    public void setPageIndex(int index) {
        this.pageIndex = index;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MenuItem> items() {
        return pageItems;
    }

    public int pageIndex() {
        return pageIndex;
    }

    public ItemMenu getPageMenu() {
        Menu pageMenu = new Menu(itemPageTitleFormat.replace("{title}", title).replace("{pageIndex}", "" + pageIndex + 1), Menus.getRows(pageItems.size()));

        pageMenu.setPage(pageIndex);

        for (MenuBehaviour behaviourOpen : behaviours.get(MenuAction.OPEN)) {
            pageMenu.addBehaviour(MenuAction.OPEN, behaviourOpen);
        }

        for (MenuBehaviour behaviourClose : behaviours.get(MenuAction.CLOSE)) {
            pageMenu.addBehaviour(MenuAction.CLOSE, behaviourClose);
        }

        return pageMenu;
    }
}
