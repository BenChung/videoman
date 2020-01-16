package sample.model.binding;

import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import sample.model.Lecture;
import sample.model.Recording;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.HashMap;

public class BidiMapAdapter extends XmlAdapter<HashMap<Lecture, Recording>, DualHashBidiMap<Lecture, Recording>> {
    @Override
    public DualHashBidiMap<Lecture, Recording> unmarshal(HashMap<Lecture, Recording> lectureRecordingHashMap) throws Exception {
        DualHashBidiMap<Lecture, Recording> map = new DualHashBidiMap<>();
        map.putAll(lectureRecordingHashMap);
        return map;
    }

    @Override
    public HashMap<Lecture, Recording> marshal(DualHashBidiMap<Lecture, Recording> lectureRecordingDualHashBidiMap) throws Exception {
        return new HashMap<Lecture, Recording>(lectureRecordingDualHashBidiMap);
    }
}
