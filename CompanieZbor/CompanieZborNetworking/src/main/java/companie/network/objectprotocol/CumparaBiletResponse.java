package companie.network.objectprotocol;

import companie.network.dto.ZborDTO;

public class CumparaBiletResponse implements UpdateResponse {
    private ZborDTO zbor;
    private int nrLocuriCumparate;

    CumparaBiletResponse(ZborDTO zbor, int nrLocuriCumparate) {
        this.zbor = zbor;
        this.nrLocuriCumparate = nrLocuriCumparate;
    }

    public ZborDTO getZbor() {
        return zbor;
    }

    public int getNrLocuriCumparate() {
        return nrLocuriCumparate;
    }
}
