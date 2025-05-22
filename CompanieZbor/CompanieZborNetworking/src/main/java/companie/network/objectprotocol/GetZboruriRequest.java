package companie.network.objectprotocol;

public class GetZboruriRequest implements Request {
    private String destinatie;
    private String dataPlecarii;

    public GetZboruriRequest(String destinatie, String dataPlecarii) {
        this.destinatie = destinatie;
        this.dataPlecarii = dataPlecarii;
    }

    public String getDestinatie() {
        return destinatie;
    }

    public String getDataPlecarii() {
        return dataPlecarii;
    }
}
