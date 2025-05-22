package companie.restservices;

import companie.model.Zbor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.jdbc.ZborRepositoryJDBC;

import java.util.Collection;

@RestController
@RequestMapping("companie/zbor-requests")
public class CompanieRestController {
    @Autowired
    private ZborRepositoryJDBC repository;

    @GetMapping("/test")
    public  String test(@RequestParam(value="name", defaultValue="Hello") String name) {
        return name.toUpperCase();
    }

    @PostMapping
    public Zbor create(@RequestBody Zbor zbor){
        System.out.println("Creating computerRepairRequest");
        return repository.add(zbor);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        System.out.println("Get by id "+id);
        Zbor request=repository.findOne(id);
        if (request==null)
            return new ResponseEntity<String>("Entity not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Zbor>(request, HttpStatus.OK);
    }

    @GetMapping
    public Collection<Zbor> filterByStatus(@RequestParam(value="destinatie", required=false)String destinatie, @RequestParam (value="dataPlecarii", required=false)String dataPlecarii) {
        Collection<Zbor> all;
        if(dataPlecarii!=null && destinatie!=null){
            System.out.println("Filtering zboruri by dataPlecarii "+ dataPlecarii + " destinatie "+ destinatie);
            all= (Collection<Zbor>) repository.filter(destinatie, dataPlecarii);
        } else{
            all= (Collection<Zbor>) repository.getAll();
        }
        return all;

    }
}
