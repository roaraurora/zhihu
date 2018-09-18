package com.zhihu.demo.vo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PageVo {

    @NotNull
    @Min(0)
    private int page;

    @NotNull
    @Min(0)
    private int offset;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
