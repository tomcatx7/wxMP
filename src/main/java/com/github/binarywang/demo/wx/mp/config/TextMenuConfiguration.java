package com.github.binarywang.demo.wx.mp.config;

import com.github.binarywang.demo.wx.mp.business.menu.constant.MenuType;
import com.github.binarywang.demo.wx.mp.business.menu.handler.ChatMenuHandler;
import com.github.binarywang.demo.wx.mp.business.menu.handler.FoodMenuHandler;
import com.github.binarywang.demo.wx.mp.business.menu.handler.NoteMenuHandler;
import com.github.binarywang.demo.wx.mp.business.menu.router.MenuRouter;
import com.github.binarywang.demo.wx.mp.business.menu.session.ISessionManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class TextMenuConfiguration {

    private final ISessionManager iSessionManager;

    private final FoodMenuHandler foodMenuHandler;

    private final NoteMenuHandler noteMenuHandler;

    private final ChatMenuHandler chatMenuHandler;

    /**
     * 自定义的文字菜单路由
     *
     * @return
     */
    @Bean
    public MenuRouter menuRouter() {
        final MenuRouter newMenuRouter = new MenuRouter();
        newMenuRouter.setiSessionManager(iSessionManager);
        newMenuRouter.rule().type(MenuType.FOODMENU).setHandler(this.foodMenuHandler).end();
        newMenuRouter.rule().type(MenuType.NOTEMENU).setHandler(this.noteMenuHandler).end();
        newMenuRouter.rule().type(MenuType.CHATMENU).setHandler(this.chatMenuHandler).end();
        return newMenuRouter;
    }
}
