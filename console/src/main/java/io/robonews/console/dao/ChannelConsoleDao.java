package io.robonews.console.dao;

import io.robonews.console.datatable.Datatable;
import io.robonews.console.datatable.DatatableCriteria;
import io.robonews.console.dto.ChannelDatatableItem;
import io.robonews.dao.ChannelDao;
import io.robonews.domain.Channel;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChannelConsoleDao extends AbstractTableDao {

    @Autowired
    private ChannelDao channelDao;

    private String[] SEARCH_FIELDS = new String[] {"name", "url"};

    @Transactional(readOnly = true)
    public Datatable<ChannelDatatableItem> getDatatable(DatatableCriteria criteria) {

        Datatable<ChannelDatatableItem> datatable = new Datatable<ChannelDatatableItem>();

        datatable.setDraw(criteria.getDraw());
        datatable.setRecordsTotal(channelDao.getCountAll());
        datatable.setRecordsFiltered(channelDao.getTableCount(criteria.getSearch(), SEARCH_FIELDS));

        List<Channel> list = channelDao.getTable(
            criteria.getStart(),
            criteria.getLength(),
            criteria.getSortField(),
            criteria.getSortDirection() == DatatableCriteria.SortDirection.ASC,
            criteria.getSearch(),
            SEARCH_FIELDS
        );

        for (Channel channel : list) {
            datatable.getData().add(
                ChannelDatatableItem.fromChannel(channel)
            );
        }

        return datatable;

    }
}
