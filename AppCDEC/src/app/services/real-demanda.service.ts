import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DemandaDTO } from '../dtos/demanda.dto';
import { ApiResponse } from '../response/api-response.dto';

@Injectable({
  providedIn: 'root'
})
export class RealDemanda {
  private apiUrl = 'http://localhost:8080/demandas'; // substitua pelo seu backend

  constructor(private http: HttpClient) {}

  // Lista todas as demandas
  getAllDemands(): Observable<DemandaDTO[]> {
      return this.http.get<ApiResponse<DemandaDTO[]>>(this.apiUrl)
        .pipe(map(response => response.data));
    }

  getDemandById(id: string): Observable<DemandaDTO> {
    return this.http.get<ApiResponse<DemandaDTO>>(`${this.apiUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  getDemandsByCitizen(citizenId: string): Observable<DemandaDTO[]> {
    return this.http.get<ApiResponse<DemandaDTO[]>>(`${this.apiUrl}?citizenId=${citizenId}`)
      .pipe(map(response => response.data));
  }
}