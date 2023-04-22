package com.desafio.desafio.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.desafio.desafio.dominio.Membro;
import com.desafio.desafio.dominio.Projeto;
import com.desafio.desafio.dominio.repositorio.MembroRepositorio;
import com.desafio.desafio.dominio.repositorio.ProjetoRepositorio;

@Controller
public class MembroController {
    
    @Autowired
    MembroRepositorio membroRepositorio;

    @Autowired
	private ProjetoRepositorio projetoRepositorio; 

    @RequestMapping(value = "/membro/salvar", method = RequestMethod.POST)
    public ResponseEntity<Object> salvar(@RequestBody Membro membro){
        
        if(membroRepositorio.findByNome(membro.getNome()).isEmpty()){
            membroRepositorio.save(membro);
        }else{
            return ResponseEntity.status(HttpStatus.valueOf(400)).body("Membro já existe com esse nome!");    
        }
        
        return ResponseEntity.ok().body(membro);
    }

    @RequestMapping("/adicionar-membro-projeto/{idMembro}/{idProjeto}")
	public String addMembroProjeto(@PathVariable(name = "idMembro")  String idMembro, 
                                        @PathVariable(name = "idProjeto")  String idProjeto) {
        
		Projeto projeto = projetoRepositorio.findById(Integer.valueOf(idProjeto)).get();
        Membro membro = membroRepositorio.findById(Integer.valueOf(idMembro)).get();

        try {
            projeto.adicionarMembro(membro);
            projetoRepositorio.save(projeto);

            return "redirect:/";		
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
	} 

    @RequestMapping("/remover-membro-projeto/{idMembro}/{idProjeto}")
	public String removerMembroProjeto(@PathVariable(name = "idMembro") int idMembro, @PathVariable(name = "idProjeto") int idProjeto) {
        
		Projeto projeto = projetoRepositorio.findById(idProjeto).get();
        Membro membro = membroRepositorio.findById(idMembro).get();

        try {
            projeto.removerMembro(membro);
            projetoRepositorio.save(projeto);
            
            return "redirect:/";		
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
	} 

}
