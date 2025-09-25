import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface Habitacion { id:number; nombre:string; precio:number; foto:string; /* ... */ }

@Injectable({ providedIn: 'root' })
export class HabitacionesService {
  private http = inject(HttpClient);
  private baseUrl = '/api/habitaciones'; // usa proxy en dev
  list() { return this.http.get<Habitacion[]>(this.baseUrl); }
}
