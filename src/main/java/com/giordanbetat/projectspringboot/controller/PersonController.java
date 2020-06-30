package com.giordanbetat.projectspringboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.giordanbetat.projectspringboot.model.Person;
import com.giordanbetat.projectspringboot.model.Telephone;
import com.giordanbetat.projectspringboot.repository.PersonRepository;
import com.giordanbetat.projectspringboot.repository.ProfessionRepository;
import com.giordanbetat.projectspringboot.repository.TelephoneRepository;

@Controller
public class PersonController {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private TelephoneRepository telephoneRepository;

	@Autowired
	private ReportUtil report;

	@Autowired
	private ProfessionRepository professionRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/register")
	public ModelAndView init() {

		ModelAndView andView = new ModelAndView("registers/register");

		andView.addObject("persons", personRepository.findAll(PageRequest.of(0, 5, Sort.by("name"))));
		andView.addObject("personobj", new Person());
		andView.addObject("professions", professionRepository.findAll());

		return andView;

	}

	@GetMapping(value = "/personspage")
	public ModelAndView chargePersonPagination(@PageableDefault(size = 5) Pageable pageable, ModelAndView andView,
			@RequestParam(value = "name") String name) {

		Page<Person> page = personRepository.findPersonByNamePage(name, pageable);
		andView.addObject("persons", page);
		andView.addObject("personobj", new Person());
		andView.addObject("name", name);
		andView.setViewName("registers/register");

		return andView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "**/save", consumes = { "multipart/form-data" })
	public ModelAndView save(@Valid Person person, BindingResult result, final MultipartFile file) throws IOException {

		if (result.hasErrors()) {
			ModelAndView andView = new ModelAndView("registers/register");

			andView.addObject("persons", personRepository.findAll(PageRequest.of(0, 5, Sort.by("name"))));
			andView.addObject("personobj", person);
			andView.addObject("professions", professionRepository.findAll());

			List<String> msg = new ArrayList<String>();

			for (ObjectError error : result.getAllErrors()) {
				msg.add(error.getDefaultMessage());
			}

			andView.addObject("msg", msg);

			return andView;
		}

		if (file.getSize() > 0) {
			person.setCurriculum(file.getBytes());
			person.setFileTypeCurriculum(file.getContentType());
			person.setFileNameCurriculum(file.getOriginalFilename());
		} else {
			if (person.getId() != null && person.getId() > 0) {

				Person personAux = personRepository.findById(person.getId()).get();
				person.setCurriculum(personAux.getCurriculum());
				person.setFileTypeCurriculum(personAux.getFileTypeCurriculum());
				person.setFileNameCurriculum(personAux.getFileNameCurriculum());
			}
		}

		personRepository.save(person);

		ModelAndView andView = new ModelAndView("registers/register");
		andView.addObject("persons", personRepository.findAll(PageRequest.of(0, 5, Sort.by("name"))));
		andView.addObject("personobj", new Person());
		andView.addObject("professions", professionRepository.findAll());

		return andView;

	}

	@GetMapping(value = "/editperson/{idperson}")
	public ModelAndView edit(@PathVariable("idperson") Long idperson) {

		Optional<Person> person = personRepository.findById(idperson);
		ModelAndView andView = new ModelAndView("registers/register");
		andView.addObject("persons", personRepository.findAll(PageRequest.of(0, 5, Sort.by("name"))));
		andView.addObject("personobj", person.get());
		andView.addObject("professions", professionRepository.findAll());

		return andView;

	}

	@GetMapping(value = "/removeperson/{idperson}")
	public ModelAndView remove(@PathVariable("idperson") Long idperson) {

		personRepository.deleteById(idperson);

		ModelAndView andView = new ModelAndView("registers/register");
		andView.addObject("persons", personRepository.findAll(PageRequest.of(0, 5, Sort.by("name"))));
		andView.addObject("personobj", new Person());
		andView.addObject("professions", professionRepository.findAll());

		return andView;

	}

	@PostMapping(value = "**/searchperson")
	public ModelAndView searchByName(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "genre", required = false) String genre,
			@PageableDefault(size = 5, sort = { "name" }) Pageable pageable) {

		Page<Person> persons = null;

		if (genre != null && !genre.isEmpty()) {
			persons = personRepository.findPersonByNameAndGenrePage(name, genre, pageable);

		}

		if (name == null) {
			persons = personRepository.findPersonByGenrePage(genre, pageable);
		} else {
			persons = personRepository.findPersonByNamePage(name, pageable);
		}

		ModelAndView andView = new ModelAndView("registers/register");
		andView.addObject("persons", persons);
		andView.addObject("personobj", new Person());
		andView.addObject("professions", personRepository.findAll(PageRequest.of(0, 5, Sort.by("name"))));
		andView.addObject("name", name);

		return andView;

	}

	@GetMapping(value = "**/printreport")
	public void printReport(@RequestParam(value = "name", required = false) String name, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		List<Person> persons = new ArrayList<Person>();

		if (name != null && !name.isEmpty()) {
			persons = personRepository.findPersonByName(name);
		} else {
			persons = (List<Person>) personRepository.findAll();
		}

		byte[] pdf = report.generateReport(persons, "person", request.getServletContext());

		response.setContentLength(pdf.length);

		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", "report.pdf");

		response.setHeader(headerKey, headerValue);

		response.getOutputStream().write(pdf);

	}

	@GetMapping(value = "/phones/{idperson}")
	public ModelAndView telephone(@PathVariable("idperson") Long idperson) {

		Optional<Person> person = personRepository.findById(idperson);
		ModelAndView andView = new ModelAndView("registers/phones");
		andView.addObject("persons", personRepository.findAll(PageRequest.of(0, 5, Sort.by("name"))));
		andView.addObject("personobj", person.get());
		andView.addObject("phones", telephoneRepository.getTelephones(idperson));

		return andView;

	}

	@PostMapping("**/registerphone/{idperson}")
	public ModelAndView registerPhone(Telephone telephone, @PathVariable("idperson") Long idperson) {

		Person person = personRepository.findById(idperson).get();

		if (telephone != null && telephone.getNumber().isEmpty() || telephone.getType().isEmpty()) {

			ModelAndView andView = new ModelAndView("registers/phones");
			andView.addObject("personobj", person);
			andView.addObject("phones", telephoneRepository.getTelephones(idperson));

			List<String> msg = new ArrayList<String>();
			if (telephone.getNumber().isEmpty()) {

				msg.add("Numero deve ser informado");
			}
			if (telephone.getType().isEmpty()) {
				msg.add("Tipo deve ser informado");
			}

			andView.addObject("msg", msg);

			return andView;
		}

		telephone.setPerson(person);
		telephoneRepository.save(telephone);

		ModelAndView andView = new ModelAndView("registers/phones");
		andView.addObject("personobj", person);
		andView.addObject("phones", telephoneRepository.getTelephones(idperson));
		return andView;
	}

	@GetMapping(value = "/removephone/{idphone}")
	public ModelAndView removePhone(@PathVariable("idphone") Long idphone) {

		Person person = telephoneRepository.findById(idphone).get().getPerson();

		telephoneRepository.deleteById(idphone);

		ModelAndView andView = new ModelAndView("registers/phones");
		andView.addObject("personobj", person);
		andView.addObject("phones", telephoneRepository.getTelephones(person.getId()));
		return andView;

	}

	@GetMapping("**/downloadCurriculum/{idperson}")
	public void downloadCurriculum(@PathVariable("idperson") Long idperson, HttpServletResponse response)
			throws IOException {

		Person person = personRepository.findById(idperson).get();

		if (person.getCurriculum() != null) {

			response.setContentLength(person.getCurriculum().length);
			response.setContentType(person.getFileTypeCurriculum());

			String headerKey = "Content-Diposition";
			String headerValue = String.format("attachment; filename=\"%s\"", person.getFileNameCurriculum());

			response.setHeader(headerKey, headerValue);

			response.getOutputStream().write(person.getCurriculum());
		}

	}

}
