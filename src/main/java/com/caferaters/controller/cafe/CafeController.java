package com.caferaters.controller.cafe;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.caferaters.model.Cafe;
import com.caferaters.model.CafeRating;
import com.caferaters.model.Dish;
import com.caferaters.service.cafe.CafeService;
import com.caferaters.service.dish.DishService;
import com.caferaters.service.vote.VoteException;
import com.caferaters.service.vote.VoteService;

@RestController
@RequestMapping("/api/cafe")
public class CafeController {

	private static final Logger logger = LoggerFactory.getLogger(CafeController.class);

	@Autowired
	private CafeService cafeService;

	@Autowired
	private DishService dishService;

	@Autowired
	private VoteService voteService;

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Cafe>> getAllCafes() {
		logger.info("Loading all cafes");

        Collection<Cafe> cafes = cafeService.getAllCafes();
        if (cafes.isEmpty()) {
        	logger.warn("No cafes found");
            return new ResponseEntity<Collection<Cafe>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Collection<Cafe>>(cafes, HttpStatus.OK);
	}

	@RequestMapping(value = "/{cafeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cafe> getCafe(@PathVariable("cafeId") long id) {
		logger.info("Fetching cafe with id {}", id);

		Cafe cafe = cafeService.getCafe(id);
		if (cafe == null) {
			logger.warn("Cafe with id {} not found", id);
			return new ResponseEntity<Cafe>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cafe>(cafe, HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createCafe(@RequestBody Cafe cafe, UriComponentsBuilder ucBuilder) {
		logger.info("Creating cafe {}", String.valueOf(cafe));

        if (cafeService.isCafeExists(cafe)) {
        	logger.warn("Cafe with name '{}' already exists", cafe.getName());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        Cafe savedCafe = cafeService.createCafe(cafe);
        
        if (savedCafe == null) {
        	logger.warn("Cafe with name '{}' not saved", cafe.getName());
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

    	HttpHeaders headers = new HttpHeaders();
    	headers.setLocation(ucBuilder.path("/api/cafe/{id}").buildAndExpand(savedCafe.getId()).toUri());
        
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{cafeId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cafe> updateCafe(@PathVariable("cafeId") long id, @RequestBody Cafe cafe) {
    	logger.info("Updating cafe with id {}", id);
        Cafe existingCafe = cafeService.getCafe(id);

        if (existingCafe == null) {
        	logger.warn("Cafe with id {} not found", cafe.getId());
            return new ResponseEntity<Cafe>(HttpStatus.NOT_FOUND);
        }

        existingCafe.setName(cafe.getName());

        cafeService.updateCafe(existingCafe);

        return new ResponseEntity<Cafe>(existingCafe, HttpStatus.OK);
    }

    @RequestMapping(value = "/{cafeId}", method = RequestMethod.DELETE)
    public ResponseEntity<Cafe> deleteCafe(@PathVariable("cafeId") long id) {
    	logger.info("Deleting cafe with id {}", id);
    	Cafe cafe = cafeService.getCafe(id);

        if (cafe == null) {
        	logger.warn("Cafe with id {} not found", id);
            return new ResponseEntity<Cafe>(HttpStatus.NOT_FOUND);
        }

        cafeService.deleteCafe(id);

        return new ResponseEntity<Cafe>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{cafeId}/menu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Dish>> getCafeMenu(@PathVariable("cafeId") long cafeId) {
    	logger.info("Getting menu in cafe with id {}", cafeId);

    	Collection<Dish> menu = dishService.getCafeMenu(cafeId);
        if (menu.isEmpty()) {
        	logger.warn("No menu found in cafe {}", cafeId);
            return new ResponseEntity<Collection<Dish>>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<Collection<Dish>>(menu, HttpStatus.OK);
    }

    @RequestMapping(value = "/{cafeId}/vote", method = RequestMethod.POST)
    public ResponseEntity<Void> voteForCafe(@PathVariable("cafeId") long cafeId, UriComponentsBuilder ucBuilder, HttpServletRequest request) {
    	logger.info("Voting for cafe with id {}", cafeId);

    	try {
    		voteService.voteForCafe(cafeId, request.getRemoteAddr());
    	} catch (VoteException e) {
        	logger.error("Voting for cafe with id {} failed: {}", cafeId, ExceptionUtils.getMessage(e));
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    	}

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/cafe/rating").buildAndExpand().toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rating", method = RequestMethod.GET)
    public ResponseEntity<Collection<CafeRating>> getCafesByRating() {
    	final Collection<CafeRating> cafeRating = cafeService.calculateCafeRating();
    	return new ResponseEntity<Collection<CafeRating>>(cafeRating, HttpStatus.OK);
    }
}
