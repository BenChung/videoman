package sample.model.binding;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;

public class ObservableListAdapter extends XmlAdapter<ObservableListAdapter.MarshalledObsList, ObservableList<?>> {
    @Override
    public ObservableList<?> unmarshal(MarshalledObsList marshalledObsList) throws Exception {
        return FXCollections.observableArrayList(marshalledObsList.elems);
    }

    @Override
    public MarshalledObsList marshal(ObservableList<?> objects) throws Exception {
        MarshalledObsList outp = new MarshalledObsList();
        outp.elems.addAll(objects);
        return outp;
    }

    public static class MarshalledObsList {
        @XmlElement(name="list")
        List<Object> elems = new ArrayList<>();

        public List<Object> getElems() { return elems; }
    }
}
