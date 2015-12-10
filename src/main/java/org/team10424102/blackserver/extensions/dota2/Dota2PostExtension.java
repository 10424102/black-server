package org.team10424102.blackserver.extensions.dota2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.team10424102.blackserver.extensions.PostExtension;
import org.team10424102.blackserver.extensions.PostExtensionIdentifier;

@PostExtensionIdentifier("dota2_match_result")
@Component
public class Dota2PostExtension implements PostExtension {

    @Autowired Dota2MatchResultRepo resultRepo;

    @Autowired ApplicationContext context;

    @Override
    public Object getData(String stub) {
        Long id = Long.parseLong(stub);
        Dota2MatchResult result = resultRepo.findOne(id);

        if (null != result.getHero()) {
            Dota2Hero hero = result.getHero();
            hero.setHeroName(
                    context.getMessage("dota2.hero." + hero.getIdentifier(), null, "", LocaleContextHolder.getLocale())
            );
        }
        return result;
    }
}
