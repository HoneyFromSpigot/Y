package io.github.thewebcode.yplugin.chat.menu;

import java.util.LinkedList;

public class ChatPages {

    final String[] data;
    final int itemsPerPage;

    public ChatPages(String[] data, int itemsPerPage) {
        this.data = data;
        this.itemsPerPage = Math.abs(itemsPerPage);
    }

    public String[] getStringsToSend(int page) {
        int startIndex = this.itemsPerPage * (Math.abs(page) - 1);
        LinkedList<String> list = new LinkedList<>();
        if (page <= this.getPages()) {
            for (int i = startIndex; i < (startIndex + this.itemsPerPage); i++) {
                if (this.data.length > i) {
                    list.add(data[i]);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }

    public int getPages() {
        return (int) Math.ceil((double) data.length / (double) this.itemsPerPage);
    }

    public int getRawArrayLength() {
        return this.data.length;
    }

}
