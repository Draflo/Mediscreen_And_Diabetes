package mediscreen.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.stereotype.Service;

import mediscreen.model.IdAuto;

@Service
public class IdService {
	
	private MongoOperations mongoOperations;
	
	@Autowired
	public IdService(MongoOperations mongoOperations) {
		this.mongoOperations = mongoOperations;
	}
	
	public long generateId(String seqName) {
	    IdAuto counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
	      new Update().inc("seq",1), options().returnNew(true).upsert(true),
	      IdAuto.class);
	    return !Objects.isNull(counter) ? counter.getSeq() : 1;
	}

}
