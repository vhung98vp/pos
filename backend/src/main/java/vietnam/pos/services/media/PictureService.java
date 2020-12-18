package vietnam.pos.services.media;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import vietnam.pos.models.media.Picture;

@Service
public class PictureService {

    //Picture
    public String getPictureUri(Picture picture) {
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(picture.getBinaryData(), false)));
        return sb.toString();
    }
}
