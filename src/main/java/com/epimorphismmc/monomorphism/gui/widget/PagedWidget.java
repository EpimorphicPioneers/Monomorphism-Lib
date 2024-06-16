package com.epimorphismmc.monomorphism.gui.widget;

import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.lowdraglib.utils.Size;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class PagedWidget extends WidgetGroup {
    @Getter
    private int pageIndex;

    protected List<Widget> pageList = new ArrayList<>();

    public PagedWidget() {
        /**/
    }

    public PagedWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public PagedWidget(Position position) {
        super(position);
    }

    public PagedWidget(Position position, Size size) {
        super(position, size);
    }

    public PagedWidget addPage(Widget widget) {
        if (widget != null && !pageList.contains(widget)) {
            pageList.add(widget);
            if (pageList.indexOf(widget) == 0) this.addWidget(widget);
        }
        return this;
    }

    public void pageUp() {
        removeWidget(pageList.get(pageIndex));
        if (--pageIndex < 0) {
            pageIndex = pageList.size() - 1;
        }
        addWidget(pageList.get(pageIndex));
    }

    public void pageDown() {
        removeWidget(pageList.get(pageIndex));
        if (++pageIndex >= pageList.size()) {
            pageIndex = 0;
        }
        addWidget(pageList.get(pageIndex));
    }
}
