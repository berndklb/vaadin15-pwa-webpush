import {customElement, html, LitElement, unsafeCSS} from 'lit-element';

import '@vaadin/vaadin-button/vaadin-button';
import '@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout';
import '@vaadin/vaadin-ordered-layout/vaadin-vertical-layout';

// utilities to import style modules
import { CSSModule } from '../../css-utils';

// @ts-ignore
import styles from './dummy-view.css';

@customElement('dummy-view')
export class DummyViewElement extends LitElement {
  static get styles() {
    return [
      CSSModule('lumo-typography'),
      unsafeCSS(styles)
    ];
  }

  render() {
    return html`
      <vaadin-vertical-layout id="wrapper" theme="padding">
        <h1>Form</h1>
        <vaadin-horizontal-layout class="button-layout" theme="spacing">
          <vaadin-button theme="tertiary" slot="" @click="${this.clearForm}">
            Cancel
          </vaadin-button>
          <vaadin-button theme="primary" @click="${this.save}">
            Save
          </vaadin-button>
        </vaadin-horizontal-layout>
      </vaadin-vertical-layout>
    `;
  }

  private async save() {
  	console.log("save pressed");
  }

  private clearForm() {
  	console.log("clear form pressed");
  }
}
