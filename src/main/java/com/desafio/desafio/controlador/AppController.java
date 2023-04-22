package com.desafio.desafio.controlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.desafio.desafio.dominio.ClassificacaoProjetoEnum;
import com.desafio.desafio.dominio.Membro;
import com.desafio.desafio.dominio.Projeto;
import com.desafio.desafio.dominio.StatusMomento;
import com.desafio.desafio.dominio.repositorio.MembroRepositorio;
import com.desafio.desafio.dominio.repositorio.ProjetoRepositorio;

import net.minidev.json.JSONObject;


@Controller
public class AppController {

	@Autowired
	private ProjetoRepositorio projetoRepositorio; 

	@Autowired
	private MembroRepositorio membroRepositorio;
	
	@RequestMapping("/")
	public ModelAndView irIndex() {
		ModelAndView mav = new ModelAndView("index");

		List<Projeto> projetoLista = new ArrayList<Projeto>();

		Iterator<Projeto> iterator = projetoRepositorio.findAll().iterator();
		while (iterator.hasNext()) {
			projetoLista.add(iterator.next());
		}
		mav.addObject("error", "");
		mav.addObject("projetoLista", projetoLista);
		
		return mav;
	}
	
 	@RequestMapping("/novo")
	public ModelAndView novoProjeto(Model model) {
		ModelAndView mav = new ModelAndView("projeto-novo");
		Projeto projeto = new Projeto();

		List<ClassificacaoProjetoEnum> classLista = Arrays.asList(ClassificacaoProjetoEnum.values());
		model.addAttribute("classLista", classLista);
		model.addAttribute("projeto", projeto);
		
		return mav;
	}
	
	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public String saveProjeto(@ModelAttribute("projeto") Projeto projeto) {
		List<StatusMomento> listaStatus = null;

		if(projeto.getId()==null){
			projeto.colocar_EM_ANALISE();;
		}else{
			Projeto pAux = projetoRepositorio.findById(projeto.getId()).get();
			listaStatus = pAux.getStatusMomentoLista();
			projeto.setStatusMomentoLista(listaStatus);
		}

		projetoRepositorio.save(projeto);
		
		return "redirect:/";
	}

	@RequestMapping("/editar/{id}")
	public ModelAndView editarProjeto(@PathVariable(name = "id") int id) {
		ModelAndView mav = new ModelAndView("projeto-novo");
		try{

			Projeto projeto = projetoRepositorio.findById(id).get();
			
			mav.addObject("projeto", projeto);
			mav.addObject("statusProjeto", projeto.getUltimoStatusMomento().getStatus());

			List<Membro> membroLista = new ArrayList<Membro>();
			Iterator<Membro> iterator = membroRepositorio.findAll().iterator();
			while (iterator.hasNext()) {
				membroLista.add(iterator.next());
			}

			membroLista.removeAll(projeto.getMembroLista());
			mav.addObject("membroLista", membroLista);

			return mav;
		}catch(Exception e){
			return irIndex();
		}
	}
	
	@RequestMapping("/remover/{id}")
	public ModelAndView deleteProjeto(@PathVariable(name = "id") int id) {
		ModelAndView mav = null;
		mav = new ModelAndView("index");

		Projeto projeto = projetoRepositorio.findById(id).get();
		if(!projeto.isPodeExcluir()){
			mav = irIndex();

			mav.addObject("error", "Projeto não pode ser EXCLUÍDO!");
			return mav;
		}

		projetoRepositorio.deleteById(id);
		return mav;		
	} 

	@RequestMapping("/selecionar-projeto/{id}")
	public @ResponseBody String selecionarProjeto(@PathVariable(name = "id") int id){

		Projeto projeto = projetoRepositorio.findById(id).get();
		
		JSONObject obj = new JSONObject();
		obj.put("projeto", projeto);
		obj.put("statusProjeto", projeto.getUltimoStatusMomento().getStatus());
		obj.put("proximoStatus", projeto.getProximoStatusPossivel());

		return obj.toString();
	}

	@RequestMapping(value = "/dar-continuidade/{id}", method = RequestMethod.POST)
	public ModelAndView darContinuidade(@PathVariable(name = "id") Integer id) {
		ModelAndView mav = irIndex();
		

		Projeto projeto = projetoRepositorio.findById(id).get();
		projeto.darContinuidade();

		projetoRepositorio.save(projeto);
		
		return mav;
	}
}
