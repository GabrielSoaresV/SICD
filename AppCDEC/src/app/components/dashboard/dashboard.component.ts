import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../navbar/navbar.component';
import { RealDemanda } from '../../services/real-demanda.service';
import { lastValueFrom } from 'rxjs';
import { DemandaDTO } from '../../dtos/demanda.dto';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  demands: DemandaDTO[] = [];
  filteredDemands: DemandaDTO[] = [];
  loading = true;
  selectedStatus = 'all';

  constructor(
    private realDemanda: RealDemanda,
    private router: Router
  ) {}

  async ngOnInit() {
    await this.loadDemands();
  }

  // Função que realmente carrega os dados do backend
  async loadDemands() {
    try {
      this.loading = true;

      // Chama o serviço real e transforma Observable em Promise
      this.demands = await lastValueFrom(this.realDemanda.getAllDemands());

      // Para compatibilidade com o HTML
      this.filteredDemands = this.demands;

      console.log('Demandas carregadas do backend (DTO):', this.demands);

    } catch (error) {
      console.error('Erro ao carregar demandas do backend:', error);
    } finally {
      this.loading = false;
    }
  }

  // Métodos para não quebrar o template (apenas log)
  filterDemands() {
    console.log('filterDemands chamado');
  }

  onFilterChange(status: string) {
    console.log('onFilterChange chamado para status:', status);
  }

  viewDemand(demandId: number) {
    console.log('viewDemand chamado para demandId:', demandId);
  }

  getStatusText(status: string): string {
    return status;
  }

  getPriorityText(priority: string): string {
    return 'Média';
  }

  formatDate(date: string): string {
    return new Date().toLocaleDateString('pt-BR');
  }
}
