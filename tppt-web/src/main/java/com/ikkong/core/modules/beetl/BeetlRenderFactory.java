package com.ikkong.core.modules.beetl;

import com.jfinal.kit.PathKit;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.jfinal3.JFinal3BeetlRender;

import java.io.IOException;

/**
 * Created by ikkong on 2017/2/10.
 */
public class BeetlRenderFactory extends RenderFactory {
    public static GroupTemplate groupTemplate = null;

    public BeetlRenderFactory() {
        init(PathKit.getWebRootPath());
    }

    public BeetlRenderFactory(ResourceLoader resourceLoader) {
        if (groupTemplate != null) {
            groupTemplate.close();
        }
        try {

            Configuration cfg = Configuration.defaultConfiguration();
            groupTemplate = new GroupTemplate(resourceLoader, cfg);
        } catch (IOException e) {
            throw new RuntimeException("加载GroupTemplate失败", e);
        }
    }

    private void init(String root) {
        if (groupTemplate != null) {
            groupTemplate.close();
        }

        try {

            Configuration cfg = Configuration.defaultConfiguration();
            WebAppResourceLoader resourceLoader = new WebAppResourceLoader(root);
            groupTemplate = new GroupTemplate(resourceLoader, cfg);

        } catch (IOException e) {
            throw new RuntimeException("加载GroupTemplate失败", e);
        }
    }

    @Override
    public Render getRender(String view) {
        return new JFinal3BeetlRender(groupTemplate, view);
    }


}
