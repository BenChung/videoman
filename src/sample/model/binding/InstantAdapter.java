package sample.model.binding;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.Instant;

public class InstantAdapter extends XmlAdapter<Long, Instant> {
    @Override
    public Instant unmarshal(Long aLong) throws Exception {
        return Instant.ofEpochMilli(aLong);
    }

    @Override
    public Long marshal(Instant instant) throws Exception {
        return instant.toEpochMilli();
    }
}
