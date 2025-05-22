package companie.network.objectprotocol;

import companie.network.dto.TuristDTO;
import companie.network.dto.ZborDTO;

public class CumparaBiletRequest implements Request {
    private ZborDTO zbor;
    private TuristDTO[] turists;
    private int nrLocuriCumparate;

    public CumparaBiletRequest(ZborDTO zbor, TuristDTO[] turists, int nrLocuriCumparate) {
        this.zbor = zbor;
        this.turists = turists;
        this.nrLocuriCumparate = nrLocuriCumparate;
    }

    public ZborDTO getZbor() {
        return zbor;
    }

    public TuristDTO[] getTurists() {
        return turists;
    }

    public int getNrLocuriCumparate() {
        return nrLocuriCumparate;
    }
}
