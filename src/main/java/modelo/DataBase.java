package modelo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class DataBase {
	MongoClientURI uri;
	MongoClient client;
	MongoDatabase db;
	MongoCollection<Document> dbUsuarios, dbPublicaciones, dbAmistades, dbPeticiones;
	Publicacion pub;
	Document doc, aux;
	MongoCursor<Document>elementos;
	
	public DataBase() {
		uri  = new MongoClientURI("mongodb://equipo03:pis03equipo@ds113935.mlab.com:13935/equipo03"); 
        client = new MongoClient(uri);
	}
	
	  /*  +----------------------------------------------------------------------+
	   *  |                                                                      |
	   *  |                              PERSONAS                                |
	   *  |                                                                      |
	   *  +----------------------------------------------------------------------+
	   */
	
	protected boolean create(Persona p) {
		try {
			db = client.getDatabase(uri.getDatabase());
			dbUsuarios = db.getCollection("usuarios");
			doc=new Document("email",p.getEmail())
					.append("clave", p.getPassword())
					.append("username", p.getUsername())
					.append("nombre", p.getNombre())
					.append("apellidos", p.getApellidos())
					.append("direccion", p.getDireccion())
					.append("telefono", p.getTelefono())
					.append("foto", p.getFoto())
					.append("rol", p.getRol());
			dbUsuarios.insertOne(doc);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	protected boolean existeEmail(String email) {
		boolean existe = false;
		db = client.getDatabase(uri.getDatabase());
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			if(doc.get("email").toString().equalsIgnoreCase(email))existe=true;
		}
		return existe;
	}
	
	protected boolean existeUsername(String username) {
		boolean existe = false;
		db = client.getDatabase(uri.getDatabase());
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			if(doc.get("username").toString().equalsIgnoreCase(username))existe=true;
		}
		return existe;
	}
	
	protected boolean login(Persona p) throws Exception {
		boolean logueado = false;
		db = client.getDatabase(uri.getDatabase());
		Persona persona=new Persona();
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			persona.setPassword(doc.get("clave").toString());
			persona.decrypt();
			if((doc.get("username").toString().equalsIgnoreCase(p.getUsername()))&&
			   (persona.getPassword().equalsIgnoreCase(p.getPassword()))) {
				logueado=true;
			}
		}
		return logueado;
	}
	
	protected boolean delete(Persona p) {
		boolean borrado= false;
		try {
			db = client.getDatabase(uri.getDatabase());
			dbUsuarios = db.getCollection("usuarios");
			elementos = dbUsuarios.find().iterator();
			doc=new Document("email",p.getEmail())
					.append("clave", p.getPassword())
					.append("username", p.getUsername())
					.append("nombre", p.getNombre())
					.append("apellidos", p.getApellidos())
					.append("direccion", p.getDireccion())
					.append("telefono", p.getTelefono())
					.append("foto", p.getFoto())
					.append("rol", p.getRol());
			while(elementos.hasNext()) {
				aux=elementos.next();
				if((aux.get("email").toString().equalsIgnoreCase(p.getEmail()))&&
				   (aux.get("clave").toString().equalsIgnoreCase(p.getPassword()))) {
					dbUsuarios.deleteOne(doc);
					borrado=true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return borrado;
	}

	protected boolean deleteEmail(String email) {
		
		db = client.getDatabase(uri.getDatabase());
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		
		while(elementos.hasNext()) {
			aux=elementos.next();
			if((aux.get("email").toString().equalsIgnoreCase(email))) {
				dbUsuarios.deleteOne(aux);
			
			}
		}
		return true;
	}
	
	protected boolean update(Persona p){
		
		db = client.getDatabase(uri.getDatabase());
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		
		deleteEmail(p.getEmail());
		create(p);
		return true;
	}
	
	protected Persona getPersona(String username) {
		Persona p = null;
		db = client.getDatabase(uri.getDatabase());
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			if((doc.get("username").toString().equalsIgnoreCase(username))) {
				p = new Persona(doc.getString("nombre"), doc.getString("apellidos"), doc.getString("username"), doc.getString("email"), doc.getString("clave"), doc.getString("direccion"), doc.getString("telefono"), doc.getString("foto"), true, doc.getString("rol"));
			}
		}		
		return p;
	}
	
	protected Persona getPersonaByEmail(String email) {
		Persona p = null;
		db = client.getDatabase(uri.getDatabase());
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			if((doc.get("email").toString().equalsIgnoreCase(email))) {
				p = new Persona(doc.getString("nombre"), doc.getString("apellidos"), doc.getString("username"), doc.getString("email"), doc.getString("clave"), doc.getString("direccion"), doc.getString("telefono"), doc.getString("foto"), true, doc.getString("rol"));
			}
		}		
		return p;
	}
	
	protected LinkedList<Persona>getAllPersonas(){
		LinkedList<Persona> personas = new LinkedList<Persona>();
		Persona p = null;
		db = client.getDatabase(uri.getDatabase());
		dbUsuarios = db.getCollection("usuarios");
		elementos = dbUsuarios.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			p = new Persona(doc.getString("nombre"), doc.getString("apellidos"), doc.getString("username"), doc.getString("email"), doc.getString("clave"), doc.getString("direccion"), doc.getString("telefono"), doc.getString("foto"), true, doc.getString("rol"));
			personas.add(p);
		}		
		return personas;
	}

	  /*  +----------------------------------------------------------------------+
	   *  |                                                                      |
	   *  |                            PUBLICACIONES                             |
	   *  |                                                                      |
	   *  +----------------------------------------------------------------------+
	   */
	
protected boolean createPublicacion(Publicacion p) {
    try {
      db = client.getDatabase(uri.getDatabase());
      dbPublicaciones = db.getCollection("publicaciones");
      doc=new Document("username", p.getUsername())
          .append("mensaje", p.getMensaje())
          .append("compartir", p.getCompartirCon())
          .append("adjuntos", p.getAdjuntos())
          .append("fecha", p.getFecha().toString());
      dbPublicaciones.insertOne(doc);
      return true;
    }catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  
  protected Publicacion readPublicacion(String username, String fecha) {
    try {
      pub = null;
      db = client.getDatabase(uri.getDatabase());
      dbPublicaciones = db.getCollection("publicaciones");
      elementos = dbPublicaciones.find().iterator();
      while(elementos.hasNext()) {
        aux = elementos.next();
        if(aux.get("username").toString().equalsIgnoreCase(username)&&(aux.get("fecha").toString().equalsIgnoreCase(fecha))) {
          List<String>els=(List<String>)aux.get("adjuntos");
          LinkedList<String> adjs=new LinkedList<String>();
          for(int i=0; i<els.size();i++) {
            adjs.add(els.get(i));
          }
          pub=new Publicacion(aux.get("username").toString(), aux.get("mensaje").toString(), aux.get("compartir").toString(), adjs, aux.get("fecha").toString());
        }
      }
    }catch(Exception ex) {
      ex.printStackTrace();
    }
    return pub;
  }
  
  protected boolean updatePublicacion(Publicacion antigua, Publicacion nueva) {
    try {
      deletePublicacion(antigua);
      createPublicacion(nueva);
      return true;
    }catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }
  protected boolean deletePublicacion(Publicacion pub) {
    boolean borrado= false;
    db = client.getDatabase(uri.getDatabase());
    dbPublicaciones = db.getCollection("publicaciones");
    elementos = dbPublicaciones.find().iterator();
    while(elementos.hasNext()) {
      aux=elementos.next();
      System.out.println("Entra: "+aux.get("username").toString()+" "+aux.get("fecha").toString());
      if((aux.get("username").toString().equalsIgnoreCase(pub.getUsername()))&&
         (aux.get("fecha").toString().equalsIgnoreCase(pub.getFecha().toString()))) {
        dbPublicaciones.deleteOne(aux);
        borrado=true;
      }
    }
    return borrado;
  }
  
  protected boolean deletePublicacionesUsuario(String username) {
    db = client.getDatabase(uri.getDatabase());
    dbPublicaciones = db.getCollection("publicaciones");
    elementos = dbPublicaciones.find().iterator();
    System.out.println("Delete: "+pub.getUsername()+" "+pub.getFecha().toString());
    while(elementos.hasNext()) {
      aux=elementos.next();
      if((aux.get("username").toString().equalsIgnoreCase(username))) {
        dbPublicaciones.deleteOne(aux);
      }
    }
    return true;
  }
  
  protected boolean deleteAllPublicaciones() {
    db = client.getDatabase(uri.getDatabase());
    dbPublicaciones = db.getCollection("publicaciones");
    elementos = dbPublicaciones.find().iterator();
    while(elementos.hasNext()) {
      aux=elementos.next();
      dbPublicaciones.deleteOne(aux);
    }
    return true;
  }
  
  protected LinkedList<Publicacion> readPublicaciones(String username) {
    LinkedList<Publicacion>pubs = new LinkedList<Publicacion>();
    try {
      db = client.getDatabase(uri.getDatabase());
      dbPublicaciones = db.getCollection("publicaciones");
      elementos = dbPublicaciones.find().iterator();
      while(elementos.hasNext()) {
        aux = elementos.next();
        if(aux.get("username").toString().equalsIgnoreCase(username)) {
          List<String>els=(List<String>)aux.get("adjuntos");
          LinkedList<String> adjs=new LinkedList<String>();
          for(int i=0; i<els.size();i++) {
            adjs.add(els.get(i));
          }
          pubs.add(new Publicacion(aux.get("username").toString(), aux.get("mensaje").toString(), aux.get("compartir").toString(), adjs, aux.get("fecha").toString()));
        }
      }
    }catch(Exception ex) {
      ex.printStackTrace();
    }
    return pubs;
  }
  
  protected LinkedList<Publicacion> readAllPublicaciones() {
    LinkedList<Publicacion>pubs = new LinkedList<Publicacion>();
    try {
      db = client.getDatabase(uri.getDatabase());
      dbPublicaciones = db.getCollection("publicaciones");
      elementos = dbPublicaciones.find().iterator();
      while(elementos.hasNext()) {
        aux = elementos.next();
        List<String>els=(List<String>)aux.get("adjuntos");
        LinkedList<String> adjs=new LinkedList<String>();
        for(int i=0; i<els.size();i++) {
          adjs.add(els.get(i));
        }
        pubs.add(new Publicacion(aux.get("username").toString(), aux.get("mensaje").toString(), aux.get("compartir").toString(), adjs, aux.get("fecha").toString()));
      }
    }catch(Exception ex) {
      ex.printStackTrace();
    }
    return pubs;
  }
  
  /*  +----------------------------------------------------------------------+
   *  |                                                                      |
   *  |                              AMISTADES                               |
   *  |                                                                      |
   *  +----------------------------------------------------------------------+
   */
  
  protected boolean createAmistad(Amistad amistad) {
		try {
			db = client.getDatabase(uri.getDatabase());
			dbAmistades = db.getCollection("amistades");
			doc=new Document("amigo1",amistad.getAmigo1())
					.append("amigo2", amistad.getAmigo2())
					.append("fecha", amistad.getFechaAmistad().toString());
					
			dbAmistades.insertOne(doc);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
  }
  
  protected boolean sonAmigos(String amigoA, String amigoB) {
		boolean sonAmigos = false;
		db = client.getDatabase(uri.getDatabase());
		dbAmistades = db.getCollection("amistades");
		elementos = dbAmistades.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			if( ( doc.get("amigo1")==amigoA && doc.get("amigo2")==amigoB ) || 
					( doc.get("amigo1")==amigoB && doc.get("amigo2")==amigoA ) ) {
				sonAmigos=true;
			}
		}
		return sonAmigos;
  }
  
  protected LinkedList<Amistad> readAmistades(String username) {
	    LinkedList<Amistad>amistades = new LinkedList<Amistad>();
	    try {
	      db = client.getDatabase(uri.getDatabase());
	      dbAmistades = db.getCollection("amistades");
	      elementos = dbAmistades.find().iterator();
	      while(elementos.hasNext()) {
	        aux = elementos.next();
	        if( aux.get("amigo1").toString().equalsIgnoreCase(username) || 
	        		aux.get("amigo2").toString().equalsIgnoreCase(username)) {
	          amistades.add(new Amistad( aux.get("amigo1").toString(), 
	        		  aux.get("amigo2").toString(), aux.get("fechaAmistad").toString() ) );
	        }
	      }
	    }catch(Exception ex) {
	      ex.printStackTrace();
	    }
	    return amistades;
  }
  
  protected Amistad readAmistad(String amigoA, String amigoB) {
	  	Amistad amistad =  null;
	    try {
	      db = client.getDatabase(uri.getDatabase());
	      dbAmistades = db.getCollection("amistades");
	      elementos = dbAmistades.find().iterator();
	      while(elementos.hasNext()) {
	        aux = elementos.next();
	        if( ( aux.get("amigo1").toString().equalsIgnoreCase(amigoA) && 
	        		aux.get("amigo2").toString().equalsIgnoreCase(amigoB) ) || 
	        		( aux.get("amigo1").toString().equalsIgnoreCase(amigoB) && 
	        				aux.get("amigo2").toString().equalsIgnoreCase(amigoA) )) {
	          amistad = new Amistad( aux.get("amigo1").toString(), 
	        		  aux.get("amigo2").toString(), aux.get("fechaAmistad").toString() );
	        }
	      }
	    }catch(Exception ex) {
	      ex.printStackTrace();
	    }
	    return amistad;
}
  
  protected boolean deleteAmistad(Amistad amistad) {
	    boolean borrado= false;
	    db = client.getDatabase(uri.getDatabase());
	    dbAmistades = db.getCollection("amistades");
	    elementos = dbAmistades.find().iterator();
	    while(elementos.hasNext()) {
	      aux=elementos.next();
	      System.out.println("Entra: "+aux.get("amigo1").toString()+" "+aux.get("amigo2").toString()+" "+aux.get("fechaAmistad").toString());
	      if( (aux.get("amigo1").toString().equalsIgnoreCase(amistad.getAmigo1())) &&
	         (aux.get("amigo2").toString().equalsIgnoreCase(amistad.getAmigo2())) ||
	         (aux.get("amigo2").toString().equalsIgnoreCase(amistad.getAmigo1())) &&
	         (aux.get("amigo1").toString().equalsIgnoreCase(amistad.getAmigo2())) ) {
	        dbPublicaciones.deleteOne(aux);
	        borrado=true;
	      }
	    }
	    return borrado;
	  }
	
  /*  +----------------------------------------------------------------------+
   *  |                                                                      |
   *  |                              PETICIONES                              |
   *  |                                                                      |
   *  +----------------------------------------------------------------------+
   */
  
  protected boolean createPeticion(Peticion peticion) {
		try {
			db = client.getDatabase(uri.getDatabase());
			dbPeticiones = db.getCollection("peticiones");
			doc=new Document("amigo1",peticion.getId1())
					.append("amigo2", peticion.getId2())
					.append("mensaje", peticion.getMensaje())
					.append("fechaPeticion", peticion.getFechaPeticion().toString())
					.append("flag", peticion.getFlag());
					
			dbPeticiones.insertOne(doc);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
  }
  
  protected boolean hayPeticion(String amigoA, String amigoB) {
		boolean hayPeticion = false;
		db = client.getDatabase(uri.getDatabase());
		dbPeticiones = db.getCollection("peticiones");
		elementos = dbPeticiones.find().iterator();
		while(elementos.hasNext()) {
			doc=elementos.next();
			if( ( doc.get("amigo1")==amigoA && doc.get("amigo2")==amigoB ) || 
					( doc.get("amigo1")==amigoB && doc.get("amigo2")==amigoA ) ) {
				hayPeticion=true;
			}
		}
		return hayPeticion;
  }
  
  protected LinkedList<Peticion> readPeticionesDe(String username) {
	    LinkedList<Peticion>peticiones = new LinkedList<Peticion>();
	    try {
	      db = client.getDatabase(uri.getDatabase());
	      dbPeticiones = db.getCollection("peticiones");
	      elementos = dbPeticiones.find().iterator();
	      while(elementos.hasNext()) {
	        aux = elementos.next();
	        if( aux.get("amigo1").toString().equalsIgnoreCase(username) ) {
	        	peticiones.add(new Peticion( aux.get("amigo1").toString(), 
	        		  aux.get("amigo2").toString(), aux.get("mensaje").toString(), 
	        		  aux.get("fechaPeticion").toString(), aux.get("flag").toString() ) );
	        }
	      }
	    }catch(Exception ex) {
	      ex.printStackTrace();
	    }
	    return peticiones;
  }
  
  protected LinkedList<Peticion> readPeticionesA(String username) {
	    LinkedList<Peticion>peticiones = new LinkedList<Peticion>();
	    try {
	      db = client.getDatabase(uri.getDatabase());
	      dbPeticiones = db.getCollection("peticiones");
	      elementos = dbPeticiones.find().iterator();
	      while(elementos.hasNext()) {
	        aux = elementos.next();
	        if( aux.get("amigo2").toString().equalsIgnoreCase(username) ) {
	        	peticiones.add(new Peticion( aux.get("amigo1").toString(), 
	        		  aux.get("amigo2").toString(), aux.get("mensaje").toString(), 
	        		  aux.get("fechaPeticion").toString(), aux.get("flag").toString() ) );
	        }
	      }
	    }catch(Exception ex) {
	      ex.printStackTrace();
	    }
	    return peticiones;
  }
  
  protected Peticion readPeticion(String amigoA, String amigoB) {
	    Peticion peticion =  null;
	    try {
	      db = client.getDatabase(uri.getDatabase());
	      dbPeticiones = db.getCollection("peticiones");
	      elementos = dbPeticiones.find().iterator();
	      while(elementos.hasNext()) {
	        aux = elementos.next();
	        if( ( aux.get("amigo1").toString().equalsIgnoreCase(amigoA) && 
	        		aux.get("amigo2").toString().equalsIgnoreCase(amigoB) ) || 
	        		( aux.get("amigo1").toString().equalsIgnoreCase(amigoB) && 
	        				aux.get("amigo2").toString().equalsIgnoreCase(amigoA) )) {
	        	peticion = new Peticion( aux.get("amigo1").toString(), 
		        		  aux.get("amigo2").toString(), aux.get("mensaje").toString(), 
		        		  aux.get("fechaPeticion").toString(), aux.get("flag").toString() );
	        }
	      }
	    }catch(Exception ex) {
	      ex.printStackTrace();
	    }
	    return peticion;
}
  
  /* No hay m�todo deletePeticion(Peticion peticion) porque no tiene sentido
  * borrar peticiones. En lugar de eso se cambia el flag. */
  
}
