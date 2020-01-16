package sample.model.binding;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.nio.file.Path;

public class NioPathAdaptor extends XmlAdapter<String, Object> {
    @Override
    public Object unmarshal(String s) throws Exception {
        if (s == null) return null;
        return Path.of(s);
    }

    @Override
    public String marshal(Object o) throws Exception {
        if (!(o instanceof Path) && o != null) {
            throw new IllegalArgumentException("Marshalling a non-path! " + o);
        }
        if (o == null) return null;
        return ((Path)o).toAbsolutePath().toString();
    }
}
