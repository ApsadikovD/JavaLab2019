package ru.javalab.chat.protocol;

public class CmdPagination {
    private int page;

    public CmdPagination() {
    }

    public CmdPagination(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
