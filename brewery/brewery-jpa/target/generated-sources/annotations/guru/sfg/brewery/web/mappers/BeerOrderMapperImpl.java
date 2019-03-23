package guru.sfg.brewery.web.mappers;

import guru.sfg.brewery.domain.BeerOrder;
import guru.sfg.brewery.domain.BeerOrder.BeerOrderBuilder;
import guru.sfg.brewery.domain.BeerOrderLine;
import guru.sfg.brewery.domain.OrderStatusEnum;
import guru.sfg.brewery.web.model.BeerOrderDto;
import guru.sfg.brewery.web.model.BeerOrderDto.BeerOrderDtoBuilder;
import guru.sfg.brewery.web.model.BeerOrderLineDto;
import guru.sfg.brewery.web.model.BeerOrderLineDto.BeerOrderLineDtoBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-03-23T17:26:03+0530",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 11.0.1 (Oracle Corporation)"
)
@Component
public class BeerOrderMapperImpl implements BeerOrderMapper {

    @Autowired
    private DateMapper dateMapper;

    @Override
    public BeerOrderDto beerOrderToDto(BeerOrder beerOrder) {
        if ( beerOrder == null ) {
            return null;
        }

        BeerOrderDtoBuilder beerOrderDto = BeerOrderDto.builder();

        beerOrderDto.id( beerOrder.getId() );
        if ( beerOrder.getVersion() != null ) {
            beerOrderDto.version( beerOrder.getVersion().intValue() );
        }
        beerOrderDto.createdDate( dateMapper.asOffsetDateTime( beerOrder.getCreatedDate() ) );
        beerOrderDto.lastModifiedDate( dateMapper.asOffsetDateTime( beerOrder.getLastModifiedDate() ) );
        beerOrderDto.beerOrderLines( beerOrderLineSetToBeerOrderLineDtoList( beerOrder.getBeerOrderLines() ) );
        beerOrderDto.orderStatus( orderStatusEnumToOrderStatusEnum( beerOrder.getOrderStatus() ) );
        beerOrderDto.orderStatusCallbackUrl( beerOrder.getOrderStatusCallbackUrl() );
        beerOrderDto.customerRef( beerOrder.getCustomerRef() );

        return beerOrderDto.build();
    }

    @Override
    public BeerOrder dtoToBeerOrder(BeerOrderDto dto) {
        if ( dto == null ) {
            return null;
        }

        BeerOrderBuilder beerOrder = BeerOrder.builder();

        beerOrder.id( dto.getId() );
        if ( dto.getVersion() != null ) {
            beerOrder.version( dto.getVersion().longValue() );
        }
        beerOrder.createdDate( dateMapper.asTimestamp( dto.getCreatedDate() ) );
        beerOrder.lastModifiedDate( dateMapper.asTimestamp( dto.getLastModifiedDate() ) );
        beerOrder.customerRef( dto.getCustomerRef() );
        beerOrder.beerOrderLines( beerOrderLineDtoListToBeerOrderLineSet( dto.getBeerOrderLines() ) );
        beerOrder.orderStatus( orderStatusEnumToOrderStatusEnum1( dto.getOrderStatus() ) );
        beerOrder.orderStatusCallbackUrl( dto.getOrderStatusCallbackUrl() );

        return beerOrder.build();
    }

    @Override
    public BeerOrderLineDto beerOrderLineToDto(BeerOrderLine line) {
        if ( line == null ) {
            return null;
        }

        BeerOrderLineDtoBuilder beerOrderLineDto = BeerOrderLineDto.builder();

        beerOrderLineDto.id( line.getId() );
        if ( line.getVersion() != null ) {
            beerOrderLineDto.version( line.getVersion().intValue() );
        }
        beerOrderLineDto.createdDate( dateMapper.asOffsetDateTime( line.getCreatedDate() ) );
        beerOrderLineDto.lastModifiedDate( dateMapper.asOffsetDateTime( line.getLastModifiedDate() ) );
        beerOrderLineDto.orderQuantity( line.getOrderQuantity() );

        return beerOrderLineDto.build();
    }

    protected List<BeerOrderLineDto> beerOrderLineSetToBeerOrderLineDtoList(Set<BeerOrderLine> set) {
        if ( set == null ) {
            return null;
        }

        List<BeerOrderLineDto> list = new ArrayList<BeerOrderLineDto>( set.size() );
        for ( BeerOrderLine beerOrderLine : set ) {
            list.add( beerOrderLineToDto( beerOrderLine ) );
        }

        return list;
    }

    protected guru.sfg.brewery.web.model.OrderStatusEnum orderStatusEnumToOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        if ( orderStatusEnum == null ) {
            return null;
        }

        guru.sfg.brewery.web.model.OrderStatusEnum orderStatusEnum1;

        switch ( orderStatusEnum ) {
            case NEW: orderStatusEnum1 = guru.sfg.brewery.web.model.OrderStatusEnum.NEW;
            break;
            case READY: orderStatusEnum1 = guru.sfg.brewery.web.model.OrderStatusEnum.READY;
            break;
            case PICKED_UP: orderStatusEnum1 = guru.sfg.brewery.web.model.OrderStatusEnum.PICKED_UP;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + orderStatusEnum );
        }

        return orderStatusEnum1;
    }

    protected Set<BeerOrderLine> beerOrderLineDtoListToBeerOrderLineSet(List<BeerOrderLineDto> list) {
        if ( list == null ) {
            return null;
        }

        Set<BeerOrderLine> set = new HashSet<BeerOrderLine>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( BeerOrderLineDto beerOrderLineDto : list ) {
            set.add( dtoToBeerOrder( beerOrderLineDto ) );
        }

        return set;
    }

    protected OrderStatusEnum orderStatusEnumToOrderStatusEnum1(guru.sfg.brewery.web.model.OrderStatusEnum orderStatusEnum) {
        if ( orderStatusEnum == null ) {
            return null;
        }

        OrderStatusEnum orderStatusEnum1;

        switch ( orderStatusEnum ) {
            case NEW: orderStatusEnum1 = OrderStatusEnum.NEW;
            break;
            case READY: orderStatusEnum1 = OrderStatusEnum.READY;
            break;
            case PICKED_UP: orderStatusEnum1 = OrderStatusEnum.PICKED_UP;
            break;
            default: throw new IllegalArgumentException( "Unexpected enum constant: " + orderStatusEnum );
        }

        return orderStatusEnum1;
    }
}
