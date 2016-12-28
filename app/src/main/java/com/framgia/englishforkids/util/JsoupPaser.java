package com.framgia.englishforkids.util;

import android.util.Log;

import com.framgia.englishforkids.data.model.VideoModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.framgia.englishforkids.util.Constant.SONG_TYPE;


/**
 * Created by GIAKHANH on 12/28/2016.
 */

public class JsoupPaser {
    public static JsoupPaser sInstance;

    private JsoupPaser() {
    }

    public static JsoupPaser getInstance() {
        if (sInstance == null) {
            sInstance = new JsoupPaser();
        }
        return sInstance;
    }

    public List<VideoModel> paser(int type) throws IOException {
        List<VideoModel> listVideoBean = new ArrayList<>();
        VideoModel newItem;
        //select url depend on type SONG or SHORT STORY
        String url = (type == SONG_TYPE ?
                Constant.URL_ROOT + Constant.URL_SONGS :
                Constant.URL_ROOT + Constant.URL_SHORT_STORIES);
        Document doc = Jsoup.connect(url).get();
        if (doc == null) return listVideoBean;
        Element blockSong = doc.select(Constant.DIV_BLOCK_CONTENT).first();
        Elements listSongElement = blockSong.select(Constant.DIV_VIEWS_ROW);
        if (listSongElement != null) {
            for (Element item : listSongElement) {
                //get image url
                String imageUrl = item.select(Constant.IMG).first().absUrl(Constant.SRC);
                //get name
                Element nameEle = item.select("a").last();
                String name = nameEle.text();
                //get url render to get video url
                String url_vid = nameEle.attr(Constant.HREF_ATTRIB);
                //create new video bean
                newItem = new VideoModel(name, imageUrl, url_vid, type);
                //add to list
                listVideoBean.add(newItem);
            }
        }
        Log.d(this.getClass().getSimpleName(), "size " + listVideoBean.size());
        return listVideoBean;
    }

    public List<VideoModel> paserSongs() throws IOException {
        return paser(Constant.SONG_TYPE);
    }

    public List<VideoModel> paserSories() throws IOException {
        return paser(Constant.STORY_TYPE);
    }
}
