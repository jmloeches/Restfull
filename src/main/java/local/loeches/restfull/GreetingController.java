package local.loeches.restfull;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
//import java.util.logging.Logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class GreetingController {
	private static final String template = "Hello, %s!";
	final static Logger logger = LoggerFactory.getLogger(GreetingController.class);
	private final AtomicLong counter = new AtomicLong();
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ICliente clienteRepo;
    
	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}
//	template="Goodbye, %s!";
	@GetMapping("/farewell")
	public Greeting farewell(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format("Goodbye, %s!", name));
	}
	
	@GetMapping("/insertar")
	public int insertar(@RequestParam(value = "nombre") String nombre, @RequestParam(value = "email") String email ) {
		String sql = "INSERT INTO cliente (nombre, email) VALUES ('"
                + nombre +"','" + email +"')";
		System.out.println(sql);
        int rows = jdbcTemplate.update(sql);
//		int rows=1;
        if (rows > 0) {
            System.out.println("A new row has been inserted.");
        }
        return rows;
	}
	
    @GetMapping("/listar")
    public List<CCliente> listAll(Model model) {
    	
        List<CCliente> listClientes = clienteRepo.findAll();
        model.addAttribute("listClientes", listClientes);
           
        return listClientes;
    }
    
    @GetMapping("/listar/{id}")
    public ResponseEntity<CCliente> get(@PathVariable Integer id){
    	try { 
    	CCliente cliente = clienteRepo.findById(id).get();
    	return new ResponseEntity<CCliente>(cliente,HttpStatus.OK);
    	    	}
    	catch (NoSuchElementException e) {
    		return new ResponseEntity<CCliente>(HttpStatus.NOT_FOUND);
    	}
    	
    	
    	
    	
    }
	
}
