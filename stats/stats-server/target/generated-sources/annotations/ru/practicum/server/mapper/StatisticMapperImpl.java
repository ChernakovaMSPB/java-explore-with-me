package ru.practicum.server.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-21T15:24:08+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class StatisticMapperImpl implements StatisticMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" );

    @Override
    public EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        if ( endpointHit == null ) {
            return null;
        }

        EndpointHitDto endpointHitDto = new EndpointHitDto();

        if ( endpointHit.getTimestamp() != null ) {
            endpointHitDto.setTimestamp( dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168.format( endpointHit.getTimestamp() ) );
        }
        endpointHitDto.setId( endpointHit.getId() );
        endpointHitDto.setApp( endpointHit.getApp() );
        endpointHitDto.setUri( endpointHit.getUri() );
        endpointHitDto.setIp( endpointHit.getIp() );

        return endpointHitDto;
    }

    @Override
    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        if ( endpointHitDto == null ) {
            return null;
        }

        EndpointHit endpointHit = new EndpointHit();

        if ( endpointHitDto.getTimestamp() != null ) {
            endpointHit.setTimestamp( LocalDateTime.parse( endpointHitDto.getTimestamp(), dateTimeFormatter_yyyy_MM_dd_HH_mm_ss_11333195168 ) );
        }
        endpointHit.setId( endpointHitDto.getId() );
        endpointHit.setApp( endpointHitDto.getApp() );
        endpointHit.setUri( endpointHitDto.getUri() );
        endpointHit.setIp( endpointHitDto.getIp() );

        return endpointHit;
    }
}
