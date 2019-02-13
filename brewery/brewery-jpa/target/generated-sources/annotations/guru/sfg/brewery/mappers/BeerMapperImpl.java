package guru.sfg.brewery.mappers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.domain.Beer.BeerBuilder;
import guru.sfg.brewery.model.BeerDto;
import guru.sfg.brewery.model.BeerDto.BeerDtoBuilder;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-02-13T10:27:05-0500",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class BeerMapperImpl implements BeerMapper {

    @Autowired
    private DateMapper dateMapper;

    @Override
    public BeerDto beerToBeerDto(Beer beer) {
        if ( beer == null ) {
            return null;
        }

        BeerDtoBuilder beerDto = BeerDto.builder();

        beerDto.beerName( beer.getBeerName() );
        beerDto.beerStyle( beer.getBeerStyle() );
        beerDto.price( beer.getPrice() );

        return beerDto.build();
    }

    @Override
    public Beer beerDtoToBeer(BeerDto beerDto) {
        if ( beerDto == null ) {
            return null;
        }

        BeerBuilder beer = Beer.builder();

        beer.id( beerDto.getId() );
        if ( beerDto.getVersion() != null ) {
            beer.version( beerDto.getVersion().longValue() );
        }
        beer.createdDate( dateMapper.asTimestamp( beerDto.getCreatedDate() ) );
        beer.lastModifiedDate( dateMapper.asTimestamp( beerDto.getLastModifiedDate() ) );
        beer.beerName( beerDto.getBeerName() );
        beer.beerStyle( beerDto.getBeerStyle() );
        beer.price( beerDto.getPrice() );

        return beer.build();
    }
}
