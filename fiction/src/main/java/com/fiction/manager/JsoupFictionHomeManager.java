package com.fiction.manager;

import android.support.annotation.NonNull;

import com.fiction.fiction.home.model.FictionHomeModel;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * by y on 2017/3/25.
 */

public class JsoupFictionHomeManager {

    public static final int TYPE_HEADER = 0; //首页四个大图小说
    public static final int TYPE_HOT = 1;   //热门小说
    public static final int TYPE_CENTER = 2;   //中间6个小说区
    public static final int TYPE_RECENT = 3;   //最近更新小说列表
    public static final int TYPE_ADD = 4;   //最新入库小说
    public static final int TYPE_TITLE = 5;

    private static final String TYPE_TITLE_HOT = "热门小说:";
    private static final String TYPE_TITLE_RETCENT = "最近更新:";
    private static final String TYPE_TITLE_ADD = "最新入库:";

    public static final String TYPE_TITLE_XUAN_HUAN = "玄幻:";
    public static final String TYPE_TITLE_XIU_ZHEN = "修真:";
    public static final String TYPE_TITLE_DU_SHI = "都市:";
    public static final String TYPE_TITLE_CHUAN_YUE = "穿越:";
    public static final String TYPE_TITLE_WANG_YOU = "网游:";
    public static final String TYPE_TITLE_KE_HUAN = "科幻:";

    private Document document;

    private JsoupFictionHomeManager(Document document) {
        this.document = document;
    }

    public static JsoupFictionHomeManager get(@NonNull Document document) {
        return new JsoupFictionHomeManager(document);
    }

    public List<FictionHomeModel> getKswHome() {
        List<FictionHomeModel> list = new ArrayList<>();
        initHeader(list);
        return list;
    }

    /**
     * 获取首页四个小说
     */
    private void initHeader(List<FictionHomeModel> list) {
        FictionHomeModel kswModel;
        Elements select = document.select("div.item");
        for (Element element : select) {
            kswModel = new FictionHomeModel();
            kswModel.setTitle(element.select("img[src]").attr("alt"));
            kswModel.setUrl(element.select("img[src]").attr("src"));
            kswModel.setDetailUrl(element.select("a:has(img)").attr("abs:href"));
            kswModel.setMessage(element.select("dd").text());
            kswModel.type = TYPE_HEADER;
            list.add(kswModel);
        }

        initPush(list);
    }


    /**
     * 获取热门小说
     */
    private void initPush(List<FictionHomeModel> list) {
        FictionHomeModel kswModel;
        Elements select = document.select("div.r").eq(0).select("a[href]");

        FictionHomeModel hotTitle = new FictionHomeModel();
        hotTitle.setTitle(TYPE_TITLE_HOT);
        hotTitle.type = TYPE_TITLE;
        list.add(hotTitle);

        for (Element element : select) {
            kswModel = new FictionHomeModel();
            kswModel.setTitle(element.text());
            kswModel.setDetailUrl(element.attr("abs:href"));
            kswModel.type = TYPE_HOT;
            list.add(kswModel);
        }

        initCenterHeader(list);
    }

    /**
     * 获取中间6个小说区头部
     */
    private void initCenterHeader(List<FictionHomeModel> list) {
        FictionHomeModel kswHomeModel;
        Elements select = document.select("div.content");
        int size = select.size();
        for (int i = 0; i < size; i++) {

            initTitle(list, i);


            kswHomeModel = new FictionHomeModel();
            Elements topSelect = select.get(i).select("div.top");
            kswHomeModel.setUrl(topSelect.select("img[src]").attr("src"));
            kswHomeModel.setTitle(topSelect.select("img[src]").attr("alt"));
            kswHomeModel.setDetailUrl(topSelect.select("a:has(img)").attr("abs:href"));
            kswHomeModel.setMessage(topSelect.select("dd").text());
            kswHomeModel.type = TYPE_HEADER;
            list.add(kswHomeModel);

            initCenter(list, select.get(i).select("li:has(a)"));
        }

        initRecent(list);
    }

    /**
     * 添加6个小说区的title
     */
    private void initTitle(List<FictionHomeModel> list, int i) {
        FictionHomeModel pushTitle = new FictionHomeModel();
        pushTitle.type = TYPE_TITLE;
        switch (i) {
            case 0:
                pushTitle.setTitle(TYPE_TITLE_XUAN_HUAN);
                break;
            case 1:
                pushTitle.setTitle(TYPE_TITLE_XIU_ZHEN);
                break;
            case 2:
                pushTitle.setTitle(TYPE_TITLE_DU_SHI);
                break;
            case 3:
                pushTitle.setTitle(TYPE_TITLE_CHUAN_YUE);
                break;
            case 4:
                pushTitle.setTitle(TYPE_TITLE_WANG_YOU);
                break;
            case 5:
                pushTitle.setTitle(TYPE_TITLE_KE_HUAN);
                break;
        }
        list.add(pushTitle);
    }

    /**
     * 获取小说中间6个区详情
     */
    private void initCenter(List<FictionHomeModel> list, Elements elements) {
        FictionHomeModel kswHomeModel;
        Elements a = elements.select("a");
        for (Element element : a) {
            kswHomeModel = new FictionHomeModel();
            kswHomeModel.setTitle(element.text());
            kswHomeModel.setDetailUrl(element.attr("abs:href"));
            kswHomeModel.type = TYPE_CENTER;
            list.add(kswHomeModel);
        }
    }

    /**
     * 最近更新小说列表
     */
    private void initRecent(List<FictionHomeModel> list) {

        FictionHomeModel pushTitle = new FictionHomeModel();
        pushTitle.setTitle(TYPE_TITLE_RETCENT);
        pushTitle.type = TYPE_TITLE;
        list.add(pushTitle);

        FictionHomeModel kswHomeModel;
        Elements select = document.select("div#newscontent").select("div.l").select("span.s2").select("a");
        for (Element element : select) {
            kswHomeModel = new FictionHomeModel();
            kswHomeModel.setTitle(element.text());
            kswHomeModel.setDetailUrl(element.attr("abs:href"));
            kswHomeModel.type = TYPE_RECENT;
            list.add(kswHomeModel);
        }

        initAdd(list);
    }

    /**
     * 获取最新入库小说
     */
    private void initAdd(List<FictionHomeModel> list) {

        FictionHomeModel pushTitle = new FictionHomeModel();
        pushTitle.setTitle(TYPE_TITLE_ADD);
        pushTitle.type = TYPE_TITLE;
        list.add(pushTitle);

        FictionHomeModel kswHomeModel;
        Elements select = document.select("div.r").eq(1).select("a[href]");
        for (Element element : select) {
            kswHomeModel = new FictionHomeModel();
            kswHomeModel.setTitle(element.text());
            kswHomeModel.setDetailUrl(element.attr("abs:href"));
            kswHomeModel.type = TYPE_ADD;
            list.add(kswHomeModel);
        }
    }
}
