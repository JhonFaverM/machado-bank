import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { TransactionService } from '../../../core/services/transaction.service';
import { Transaction } from '../../../models/transaction.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-transaction-history',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './transaction-history.html',
  styleUrls: ['./transaction-history.css']
})
export class TransactionHistory implements OnInit {
  transactions: Transaction[] = [];
  accountNumber: string = '';
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private transactionService: TransactionService,
     private cdr: ChangeDetectorRef 
  ) {}

  ngOnInit(): void {
    // Obtenemos el número de cuenta de la URL: /history/12345
    this.accountNumber = this.route.snapshot.paramMap.get('accountNumber') || '';
    if (this.accountNumber) {
      this.loadHistory();
    }
  }

  loadHistory() {
    this.loading = true;
    this.transactionService.getHistory(this.accountNumber).subscribe({
      next: (res) => {
        this.transactions = res;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  getTransactionLabel(type: string): string {
    switch (type) {
      case 'TRANSFER': return 'Transferencia enviada';
      case 'DEPOSIT': return 'Consignación';
      case 'WITHDRAW': return 'Retiro en cajero';
      default: return 'Movimiento bancario'; // Caso por defecto por si falla algo
    }
  }
}
