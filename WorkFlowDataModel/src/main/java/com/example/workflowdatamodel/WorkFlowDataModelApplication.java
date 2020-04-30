package com.example.workflowdatamodel;

import com.example.workflowdatamodel.entity.Associates;
import com.example.workflowdatamodel.entity.Individual;
import com.example.workflowdatamodel.entity.Roles;
import com.example.workflowdatamodel.repository.AssociateRepository;
import com.example.workflowdatamodel.repository.RoleRepository;
import com.example.workflowdatamodel.service.AssociateService;
import com.example.workflowdatamodel.service.IndividualService;
import com.example.workflowdatamodel.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WorkFlowDataModelApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkFlowDataModelApplication.class, args);
	}

	private AssociateService associateService;
	private RolesService rolesService;

	@Autowired
	public void setAssociateService(AssociateService associateService) {
		this.associateService = associateService;
	}

	@Autowired
	public void setRolesService(RolesService rolesService) {
		this.rolesService = rolesService;
	}

	@Bean
	public ApplicationRunner initializer(){
		return args -> {
			try {
				Associates designer = associateService.addAssociate("light", "yagami",
						"lightyagami@flowkit.com",
						"1677", null, null);
				if(designer!=null){
					Roles admin = rolesService.addRole("Flow Kit Admin");
					rolesService.addRole("Flow Kit Designer");
					rolesService.addRole("Customer");
					associateService.updateRoleOfAssociate(designer, admin);

				}
			}catch (Exception error){
				System.out.println(error.getLocalizedMessage());
			}
		};
	}

}
