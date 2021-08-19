package guru.sfg.brewery.web.model;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeerStyleBean {

    @Bean
    public BeerStyleEnum[] returnBeerStyles() {
        return BeerStyleEnum.values();
    }
}
