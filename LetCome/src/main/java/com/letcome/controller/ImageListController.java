package com.letcome.controller;

/**
 * Created by rjt on 16/7/21.
 */

import com.letcome.entity.ImageEntity;
import com.letcome.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller

public class ImageListController {
    @Resource(name="imageService")
    private ImageService service;
    @ResponseBody
    @RequestMapping(value="/imagelist", method = RequestMethod.GET)
    public String list(ModelMap modelMap) {
        String str = "[{\"id\":3327769,\"image_width\":1600,\"image_height\":1154,\"note_id\":3654341,\"likes_count\":44,\"image_url\":\"http://p.chanyouji.cn/93740/1388042805912p18cmsjuajqo44sh1lmdf61ea61q.jpg\",\"description\":\"清水寺日落前的逆光\",\"trip_id\":93740,\"trip_name\":\"迷失东瀛\"}," +
                "{\"id\":3327652,\"image_width\":1600,\"image_height\":1194,\"note_id\":3654227,\"likes_count\":27,\"image_url\":\"http://p.chanyouji.cn/93740/1388042656447p18cmsjuaj2bcrlaqvr1cs51pf77.jpg\",\"description\":\"老树昏鸦\",\"trip_id\":93740,\"trip_name\":\"迷失东瀛\"}," +
                "{\"id\":4196665,\"image_width\":800,\"image_height\":533,\"note_id\":4591747,\"likes_count\":25,\"image_url\":\"http://p.chanyouji.cn/111229/1397135061972p18l5rntfdefsn1gtfcg91plfo.jpg\",\"description\":\"清水寺〜当年我看到一个留学日本人男生拍的照片，觉得美的想揍他。就是这番场景。\",\"trip_id\":111229,\"trip_name\":\"迷宫的十字路-京都樱花篇\"}," +
                "{\"id\":7456867,\"image_width\":1600,\"image_height\":1067,\"note_id\":8087035,\"likes_count\":22,\"image_url\":\"http://p.chanyouji.cn/186406/1415374802761p1965e0g5eefjuqouv0ud51v8e27.jpg\",\"description\":\"金色和深蓝天空的交杂！大家都看呆了\",\"trip_id\":186406,\"trip_name\":\"念念不忘 必有回响 日本二次 京都篇！\"}," +
                "{\"id\":3327781,\"image_width\":1272,\"image_height\":1600,\"note_id\":3654353,\"likes_count\":20,\"image_url\":\"http://p.chanyouji.cn/93740/1388042853415p18cmsjuaj1fpn12mg1vh31d471i6v22.jpg\",\"description\":\"逆光\",\"trip_id\":93740,\"trip_name\":\"迷失东瀛\"}," +
                "{\"id\":7710186,\"image_width\":800,\"image_height\":533,\"note_id\":8355983,\"likes_count\":18,\"image_url\":\"http://p.chanyouji.cn/190688/1417336862619p197vtm01o1uphscn2a618h01fms2d.jpg\",\"description\":\"步道边一位写生老爷爷的画，好美。\",\"trip_id\":190688,\"trip_name\":\"红叶猎狩，悠游关西\"}," +
                "{\"id\":7456871,\"image_width\":1067,\"image_height\":1600,\"note_id\":8087039,\"likes_count\":18,\"image_url\":\"http://p.chanyouji.cn/186406/1415374816759p1965e0g5emlnb4n1ns11ilf1mo029.jpg\",\"description\":\"过一会儿蓝色出现了，金色却倒映在水面上\",\"trip_id\":186406,\"trip_name\":\"念念不忘 必有回响 日本二次 京都篇！\"}," +
                "{\"id\":5206691,\"image_width\":1063,\"image_height\":1600,\"note_id\":5685442,\"likes_count\":17,\"image_url\":\"http://p.chanyouji.cn/134486/1403331649005p18quh7518uqb1p94qn6u3c18shm.jpg\",\"description\":\"今天和服大变身~和服突出的是女士颈部线条~来一个吧~\",\"trip_id\":134486,\"trip_name\":\"暴走少女+吃货少年错峰关西游\"}," +
                "{\"id\":3327773,\"image_width\":1167,\"image_height\":1600,\"note_id\":3654345,\"likes_count\":16,\"image_url\":\"http://p.chanyouji.cn/93740/1388042821860p18cmsjuajk8g1hjuml713u68801s.jpg\",\"description\":\"夜幕降临前的京都\",\"trip_id\":93740,\"trip_name\":\"迷失东瀛\"}," +
                "{\"id\":454001,\"image_width\":850,\"image_height\":564,\"note_id\":467381,\"likes_count\":15,\"image_url\":\"http://p.chanyouji.cn/15730/1354455528326p17ddskiqo1cgn53tklf18es8d61f.jpg\",\"description\":\"\",\"trip_id\":15730,\"trip_name\":\"启程，归途\"}," +
                "{\"id\":5605136,\"image_width\":1042,\"image_height\":1600,\"note_id\":6117372,\"likes_count\":14,\"image_url\":\"http://p.chanyouji.cn/144138/1405568622940p18t166n2i1qp51mtk1d141gi71010n-1405571037.jpg\",\"description\":\"在这里，到处都是穿着和服的温婉女子，我也凑个热闹来一张\",\"trip_id\":144138,\"trip_name\":\"霓虹国物语----樱开樱落的淡淡风情\"}," +
                "{\"id\":3327653,\"image_width\":1600,\"image_height\":1159,\"note_id\":3654228,\"likes_count\":14,\"image_url\":\"http://p.chanyouji.cn/93740/1388042658710p18cmsjuaj46i1eft1at4rm9ts8.jpg\",\"description\":\"有幸遇到艺伎\",\"trip_id\":93740,\"trip_name\":\"迷失东瀛\"}," +
                "{\"id\":6052755,\"image_width\":800,\"image_height\":533,\"note_id\":6595899,\"likes_count\":14,\"image_url\":\"http://p.chanyouji.cn/153646/1407666345381p18uvn3fvg1ltaftt2bk1klpqdr11.jpg\",\"description\":\"经典角度，远远的是京都塔 。\",\"trip_id\":153646,\"trip_name\":\"花见日本，春日纪行\"}," +
                "{\"id\":7456869,\"image_width\":1067,\"image_height\":1600,\"note_id\":8087037,\"likes_count\":14,\"image_url\":\"http://p.chanyouji.cn/186406/1415374808757p1965e0g5ebco1v47t1t10v6avg28.jpg\",\"description\":\"突然间天空变成大片的金色\",\"trip_id\":186406,\"trip_name\":\"念念不忘 必有回响 日本二次 京都篇！\"}," +
                "{\"id\":1672397,\"image_width\":1600,\"image_height\":1117,\"note_id\":1813935,\"likes_count\":13,\"image_url\":\"http://p.chanyouji.cn/48486/1371997349318p17tomhq2c10n0qtmm2io581dog1b.jpg\",\"description\":\"冈本和服门口的栅栏和走廊，太应景了！\",\"trip_id\":48486,\"trip_name\":\"霓虹关西の艳阳下之二\"}," +
                "{\"id\":3327774,\"image_width\":1600,\"image_height\":1308,\"note_id\":3654346,\"likes_count\":13,\"image_url\":\"http://p.chanyouji.cn/93740/1388042829568p18cmsjuaj1g8tq919ulasv11tc1t.jpg\",\"description\":\"大阪居酒屋老板，特别客气\",\"trip_id\":93740,\"trip_name\":\"迷失东瀛\"}," +
                "{\"id\":1165985,\"image_width\":899,\"image_height\":1600,\"note_id\":1232306,\"likes_count\":12,\"image_url\":\"http://p.chanyouji.cn/34062/1367849388016p17pt2osp1gtt3pc1ban2ukmh69.jpg\",\"description\":\"我好喜欢这张图！好！喜！欢！\",\"trip_id\":34062,\"trip_name\":\"门外汉的日本国\"}," +
                "{\"id\":4622167,\"image_width\":700,\"image_height\":467,\"note_id\":5056472,\"likes_count\":12,\"image_url\":\"http://p.chanyouji.cn/120660/1399557795802p18ne1orpa1spnkic1gdl4sv1027.jpg\",\"description\":null,\"trip_id\":120660,\"trip_name\":\"日本樱满开京吹雪之旅2014\"}," +
                "{\"id\":7710184,\"image_width\":800,\"image_height\":533,\"note_id\":8355981,\"likes_count\":12,\"image_url\":\"http://p.chanyouji.cn/190688/1417336860809p197vtm01o1bjqjb210l3oirpf72b.jpg\",\"description\":\"还记得上次站在这个位子拍过同样角度的夜樱。\",\"trip_id\":190688,\"trip_name\":\"红叶猎狩，悠游关西\"}," +
                "{\"id\":4622179,\"image_width\":700,\"image_height\":467,\"note_id\":5056481,\"likes_count\":12,\"image_url\":\"http://p.chanyouji.cn/120660/1399557825276p18ne1orpa19417mr1ln61qjdn3r2e.jpg\",\"description\":\"清水舞台和京都塔夜景\",\"trip_id\":120660,\"trip_name\":\"日本樱满开京吹雪之旅2014\"}]";

        return str;
    }

    @ResponseBody
    @RequestMapping(value="/waterfalls", method = RequestMethod.GET)
    public List<ImageEntity> waterfalls(@RequestParam("pno") String pno,@RequestParam("limit") String limit,HttpServletRequest request){
//        System.out.println(request.getRequestURI());
//        System.out.println(request.getRequestURL());
//        System.out.println(request.getContextPath());
//        System.out.println(request.getRealPath("/"));
//        System.out.println(request.getServletPath());
//        System.out.println(request.getServerName());
//        System.out.println(request.getServerPort());
//        System.out.println(request.getServletContext());
        return  service.getWaterfalls(Long.valueOf(pno),Long.valueOf(limit),"http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/image/getimg?id=");

    }
    
}
