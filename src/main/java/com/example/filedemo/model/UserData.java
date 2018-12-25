package com.example.filedemo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.persistence.*;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@Entity
// Файл 092018B1.xlsx
@Table(name = "user_data")
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    // Файл 092018N1
    @ManyToOne
    @JoinColumn (name="creditOrganization_id", nullable = false)
    private CreditOrganization creditOrganization;

    // Файл NAMES
    @ManyToOne
    @JoinColumn (name="score_id", nullable = false)
    private Score score;

    //Входящие остатки «в рублях», тыс. руб
    @Column(name = "incoming_balances_in_rubles")
    private Long incomingBalancesInRubles;

    //Входящие остатки «ин. вал., драг. металлы», тыс. руб
    @Column(name = "incoming_balances_drag_metals")
    private Long incomingBalancesDragMetals;

    //Входящие остатки «итого», тыс. руб.; счета Депо – в штуках
    @Column(name = "incoming_balances_of_total")
    private Double incomingBalancesOfTotal;

    //Обороты за отчетный период по дебету (активу) «в рублях», тыс. руб
    @Column(name = "turnovers_debit_in_rubles")
    private Long turnoversDebitInRubles;

    //Обороты за отчетный период по дебету (активу) «ин. вал., драг. металлы», тыс. руб
    @Column(name = "turnovers_debit_drag_metals")
    private Long turnoversDebitDragMetals;

    //Обороты за отчетный период по дебету (активу) «итого», тыс. руб.; счета Депо – в штуках
    @Column(name = "turnovers_debit_of_total")
    private Double turnoversDebitOfTotal;

    //Обороты за отчетный период по кредиту (пассиву) «в рублях», тыс. руб.
    @Column(name = "turnovers_credit_in_rubles")
    private Long turnoversCreditInRubles;

    //Обороты за отчетный период по кредиту (пассиву) «ин. вал., драг. металлы», тыс. руб.
    @Column(name = "turnovers_credit_drag_metals")
    private Long turnoversCreditDragMetals;

    //Обороты за отчетный период по кредиту (пассиву) «итого», тыс. руб.;
    @Column(name = "turnovers_credit_of_total")
    private Double turnoversCreditOfTotal;

    //Исходящие остатки «в рублях», тыс. руб.
    @Column(name = "outgoing_balances_in_rubles")
    private Long outgoingBalancesInRubles;

    //Исходящие остатки «ин. вал., драг. металлы», тыс. руб.
    @Column(name = "outgoing_balances_drag_metals")
    private Long outgoingBalancesDragMetals;

    //Исходящие остатки «итого», тыс. руб.;
    @Column(name = "outgoing_balances_of_total")
    private Double outgoingBalancesOfTotal;

}
