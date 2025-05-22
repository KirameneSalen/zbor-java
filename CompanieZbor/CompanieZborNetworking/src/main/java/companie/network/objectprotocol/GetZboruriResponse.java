package companie.network.objectprotocol;

import companie.network.dto.ZborDTO;

public class GetZboruriResponse implements Response {
    private ZborDTO[] zboruri;

    GetZboruriResponse(ZborDTO[] zboruri) {
        this.zboruri = zboruri;
    }

    public ZborDTO[] getZboruri() {
        return zboruri;
    }
}
